package com.aberon.flexbook.manager

import android.content.Context
import android.util.Base64
import com.aberon.flexbook.model.*
import com.kursx.parser.fb2.FictionBook
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.stream.Stream
import javax.xml.parsers.ParserConfigurationException


class FB2Format(context: Context) : Format {
    val COVERS_DIR = File("${context.applicationInfo.dataDir}/covers").apply {
        if (!exists()) {
            mkdir()
        }
    }

    override fun serialize(inputStream: InputStream): BookInfo? {
        val file = File.createTempFile("book", ".fb2")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return serialize(file)
    }

    fun serialize(file: File): BookInfo? {
        return try {
            val fictionBook = FictionBook(file)
            val title = fictionBook.title
            val bookId = UUID.randomUUID().toString()
            val covers = fictionBook.description.titleInfo.coverPage.map { cover ->
                val coverId = UUID.randomUUID().toString()
                Cover(coverId, bookId,
                    fictionBook.binaries[cover.value.removePrefix("#")]!!.let { binary ->
                        File("${COVERS_DIR}/${title}_${binary.id}").apply {
                            createNewFile()
                            writeBytes(Base64.decode(binary.binary, Base64.DEFAULT))
                        }.absolutePath
                    })
            }
            val authors = fictionBook.authors.map { person ->
                val authorId = UUID.randomUUID().toString()
                AuthorInfo(Author(
                    authorId, bookId,
                    person.lastName,
                    person.middleName,
                    person.firstName,
                    person.fullName,
                    person.nickname,
                ),
                    person.emails.map { email ->
                        val emailId = UUID.randomUUID().toString()
                        Email(emailId, authorId, email)
                    }
                )
            }
            val sections = fictionBook.body.sections.map { section ->
                Section(
                    section.titles?.lastOrNull()?.paragraphs?.lastOrNull()?.text,
                    null,
                    section.elements.map { element ->
                        Paragraph(
                            emptyList(),
                            element.text
                        )
                    },
                    null,
                    null,
                    emptyList()
                )
            }
            BookInfo(
                Book(
                    bookId,
                    title,
                    "DESC", // TODO DESC
                    BookType.FB2,
                    file.absolutePath,
                ).apply { this.sections = sections },
                covers,
                authors
            )
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: SAXException) {
            e.printStackTrace()
            null
        }
    }

    override fun deserialize(book: BookInfo): Stream<Byte> {
        TODO("Not yet implemented")
    }
}