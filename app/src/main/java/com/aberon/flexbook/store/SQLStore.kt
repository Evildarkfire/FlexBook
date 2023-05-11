package com.aberon.flexbook.store

import android.content.Context
import androidx.room.Room
import com.aberon.flexbook.manager.Format
import com.aberon.flexbook.model.*

class SQLStore(context: Context) {
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
        get() = db.storeDao().loadBooks().distinctBy { bookInfo -> bookInfo.book.bookId }

    val preference: Map<PreferenceKey, Preference>
        get() = db
            .storeDao()
            .loadPreferences()
            .associateBy { preference -> preference.preferenceId }

    fun getBookById(bookId: String): BookInfo {
        val bookInfo = db.storeDao().loadById(bookId)
        Format.loadBookContent(bookInfo)
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

    fun addBookParametr(bookParametr: BookParametr) {
        db.storeDao().insert(bookParametr)
    }

    fun updatePreferences(newPreferences: Map<PreferenceKey, Preference>) {
        val storedPreferences = preference
        newPreferences.forEach { newPref ->
            if (storedPreferences[newPref.key] != null) {
                db.storeDao().update(newPref.value)
            } else {
                db.storeDao().insert(newPref.value)
            }
        }
    }

    fun updateBookParametr(parametr: BookParametr) {
        db.storeDao().update(parametr)
    }

    fun deleteBook(book: Book) = db.storeDao().delete(book)
}