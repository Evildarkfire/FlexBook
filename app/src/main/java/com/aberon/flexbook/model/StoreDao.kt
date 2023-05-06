package com.aberon.flexbook.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface StoreDao {
    @Transaction
    @Query("SELECT * FROM book " +
            "LEFT JOIN covers ON covers.coverBookId == book.bookId " +
            "LEFT JOIN author ON author.ownedBookId == book.bookId " +
            "LEFT JOIN email ON email.authorId == author.authorId"
    )
    fun getAll(): List<BookInfo>

    @Transaction
    @Query("SELECT book.bookId, book.title, book.description, book.type, book.path, " +
            "covers.coverId, covers.coverPath, " +
            "author.authorId, author.firstName, author.lastName, author.middleName, " +
            "author.nickname FROM book " +
            "LEFT JOIN covers ON covers.coverBookId == book.bookId " +
            "LEFT JOIN author ON author.ownedBookId == book.bookId " +
            "LEFT JOIN email ON email.authorId == author.authorId " +
            "WHERE book.bookId = :bookId"
    )
    fun loadAllById(bookId: String): BookInfo

    @Insert
    fun insert(book: Book)

    @Insert
    fun insert(author: Author)

    @Insert
    fun insert(cover: Cover)

    @Insert
    fun insert(email: Email)

    @Insert
    fun insertAll(books: List<Book>)

    @Delete
    fun delete(book: Book)
}