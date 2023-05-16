package com.aberon.flexbook.manager

import android.text.Html
import com.aberon.flexbook.model.*
import nl.siegmann.epublib.epub.EpubReader
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.UUID

class EPubFormat : Format() {
    override fun serialize(bookInfo: BookInfo) {
        val bookFile = File(bookInfo.book.path)
        val epubBook = EpubReader().readEpub(bookFile.inputStream())
        bookInfo.book.sections = loadSections(epubBook)
    }

    override fun serialize(bookFile: File): BookInfo? {
        val epubBook = EpubReader().readEpub(bookFile.inputStream())
        val bookId = UUID.randomUUID().toString()
        val coverId = UUID.randomUUID().toString()

        val cover = Cover(
            coverId = coverId,
            coverBookId = bookId,
            coverPath = ""
        ).apply { coverBytes = epubBook.coverImage.data }

        val authors = epubBook.metadata.authors.map { author ->
            val authorId = UUID.randomUUID().toString()
            AuthorInfo(
                Author(
                    authorId = authorId,
                    ownedBookId = bookId,
                    lastName = author.lastname,
                    firstName = author.firstname,
                    middleName = null,
                    nickname = null
                ),
                emptyList()
            )
        }

        val book = Book(
            bookId = bookId,
            language = epubBook.metadata.language,
            title = epubBook.title,
            description = epubBook.metadata.descriptions.joinToString { "$it\n" },
            type = BookType.EPUB,
            path = bookFile.absolutePath
        ).apply { sections = loadSections(epubBook) }
        /*
        book.metadata
        book.guide
        book.spine
        */

        return BookInfo(
            book = book,
            covers = mutableListOf(cover),
            authors = authors,
            parametrs = mutableListOf()
        )
    }

    private fun loadSections(epubBook: nl.siegmann.epublib.domain.Book): List<Section> {
        val result =  epubBook.contents.mapIndexed { index, content ->
            val elements = when (content.mediaType.name) {
                "application/xhtml+xml" ->
                    Html.fromHtml(
                        String(content.data, StandardCharsets.UTF_8),
                        Html.FROM_HTML_MODE_COMPACT
                    )
                else -> ""
            }
                .trim()
                .splitToSequence("\n")
                .filter { it.isNotEmpty() }
                .map { text -> Paragraph(text) }
                .toList()
            Section(
                index,
                content.title,
                elements,
                emptyList()
            )
        }.toMutableList()
        result.firstOrNull()?.let {
            result.remove(it) //TODO fix
        }
        return result
    }
}