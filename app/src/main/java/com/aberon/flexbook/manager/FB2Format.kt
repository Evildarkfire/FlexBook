package com.aberon.flexbook.manager

import android.util.Base64
import com.aberon.flexbook.model.*
import com.kursx.parser.fb2.FictionBook
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import kotlin.collections.ArrayList


class FB2Format : Format() {
    override fun serialize(bookInfo: BookInfo) {
        val bookFile = File(bookInfo.book.path)
        val fictionBook = FictionBook(bookFile)
        bookInfo.book.sections = loadSections(fictionBook)
    }

    override fun serialize(bookFile: File): BookInfo? {
        return try {
            val bookId = UUID.randomUUID().toString()

            val fictionBook = FictionBook(bookFile)
            val title = fictionBook.title
            val covers = fictionBook.description.titleInfo.coverPage.map { cover ->
                val coverId = UUID.randomUUID().toString()
                Cover(
                    coverId = coverId,
                    coverBookId = bookId,
                    coverPath = "null"
                ).apply {
                    coverBytes =
                        fictionBook.binaries[cover.value.removePrefix("#")]!!.let { binary ->
                            Base64.decode(binary.binary, Base64.DEFAULT)
                        }
                }
            }
            val authors = fictionBook.authors.map { person ->
                val authorId = UUID.randomUUID().toString()
                AuthorInfo(Author(
                    authorId = authorId,
                    ownedBookId = bookId,
                    lastName = person.lastName,
                    middleName = person.middleName,
                    firstName = person.firstName,
                    nickname = person.nickname,
                ),
                    person.emails.map { email ->
                        val emailId = UUID.randomUUID().toString()
                        Email(emailId, authorId, email)
                    }
                )
            }

            val book = Book(
                bookId = bookId,
                language = fictionBook.lang,
                title = title,
                description = "DESC", // TODO DESC
                type = BookType.FB2,
                path = bookFile.absolutePath,
            ).apply { sections = loadSections(fictionBook) }

            BookInfo(
                book = book,
                covers = covers,
                authors = authors,
                parametrs = mutableListOf()
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

    private fun loadSections(fictionBook: FictionBook): List<Section> {
        return loadSections(fictionBook.body.sections).mapIndexed { index, section ->
            Section(
                id = index,
                title = section.titles?.lastOrNull()?.paragraphs?.lastOrNull()?.text,
                elements = section.elements.map { element ->
                    Paragraph(
                        element.text,
                        emptyList()
                    )
                },
                images = null
            )
        }
    }

    private fun loadSections(sections: ArrayList<com.kursx.parser.fb2.Section>)
            : ArrayList<com.kursx.parser.fb2.Section> {
        val array = arrayListOf<com.kursx.parser.fb2.Section>()
        sections.forEach { section ->
            if (section.elements.isNotEmpty()) {
                array.add(section)
            }
            if (section.sections.isNotEmpty()) {
                array.addAll(loadSections(section.sections))
            }
        }
        return array
    }
}