package com.aberon.flexbook.store

import android.content.Context
import androidx.room.Room
import com.aberon.flexbook.manager.FB2Format
import com.aberon.flexbook.model.Book
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.model.Cover
import java.io.File

class SQLStore(val context: Context) {
    companion object {
        private var instance: SQLStore? = null

        @Synchronized
        fun getInstance(context: Context) =
            instance ?: SQLStore(context).apply { instance = this }
    }

    private val DATABASE_NAME = "flexbook"
    private val db = Room.databaseBuilder(context, SQLDataBase::class.java, DATABASE_NAME)
        .allowMainThreadQueries()
        .build()

    val books: List<BookInfo>
        get() = db.storeDao().getAll()

    fun getBookById(bookId: String): BookInfo {
        val bookInfo = db.storeDao().loadAllById(bookId)
        loadBookSection(bookInfo)
        return bookInfo
    }

    fun addBook(bookInfo: BookInfo) {
        db.storeDao().insert(bookInfo.book)
        bookInfo.covers.forEach { cover ->
            db.storeDao().insert(cover)
        }
        bookInfo.authors.forEach { authorInfo ->
            db.storeDao().insert(authorInfo.author)
            authorInfo.emails.forEach { email ->
                db.storeDao().insert(email)
            }
        }
    }

    fun addCover(cover: Cover) = db.storeDao().insert(cover)
    fun addBooks(books: List<Book>) = db.storeDao().insertAll(books)
    fun deleteBook(book: Book) = db.storeDao().delete(book)

    private fun loadBookSection(bookInfo: BookInfo) {
        val f = File(bookInfo.book.path)
        if (!f.exists()) {
            throw java.lang.Exception() // TODO miss path
        }
        FB2Format(context).serialize(File(bookInfo.book.path))?.let { b ->
            bookInfo.book.sections = b.book.sections
            bookInfo.book.parameters = b.book.parameters
        }
    }
}