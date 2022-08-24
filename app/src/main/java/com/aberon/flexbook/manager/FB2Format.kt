package com.aberon.flexbook.manager

import android.graphics.BitmapFactory
import android.util.Base64
import com.aberon.flexbook.model.*
import com.kursx.parser.fb2.FictionBook
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.stream.Stream
import javax.xml.parsers.ParserConfigurationException


class FB2Format : Format {
    override fun serialize(inputStream: InputStream): Book? {
        return try {
            val file = File.createTempFile("book", ".fb2")
            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val fictionBook = FictionBook(file)
            val title = fictionBook.title
            val imageBinary = fictionBook.binaries.values.last() //TODO fictionBook.description.titleInfo.coverPage
            val encodeByte: ByteArray = Base64.decode(imageBinary.binary, Base64.DEFAULT)
            val authors = fictionBook.authors.map { person ->
                Author(
                    person.lastName ?: "",
                    person.middleName ?: "",
                    person.firstName ?: "",
                    person.fullName ?: "",
                    person.nickname ?: "",
                    person.emails ?: emptyList()
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
            Book(
                title,
                authors,
                "",
                BookType.FB2,
                BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size),
                file.absolutePath,
                sections
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

    override fun deserialize(book: Book): Stream<Byte> {
        TODO("Not yet implemented")
    }
}