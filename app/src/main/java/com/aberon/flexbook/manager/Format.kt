package com.aberon.flexbook.manager

import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.model.BookType
import java.io.File

abstract class Format {
    abstract fun serialize(bookFile: File): BookInfo?
    abstract fun serialize(bookInfo: BookInfo)

    companion object {
        fun serialize(type: String, bookFile: File): BookInfo? {
            return when (type) {
                "application/epub+zip" -> EPubFormat().serialize(bookFile)
                "application/octet-stream" -> FB2Format().serialize(bookFile)
                else -> null
            }
        }
        fun loadBookContent(bookInfo: BookInfo){
            when (bookInfo.book.type) {
                BookType.EPUB -> EPubFormat().serialize(bookInfo)
                BookType.FB2 -> FB2Format().serialize(bookInfo)
            }
        }
    }
}