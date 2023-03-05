package com.aberon.flexbook.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StoreDao {
    @Query("SELECT * FROM book " +
            "LEFT JOIN covers ON covers.bookId == book.bookId " +
            "LEFT JOIN author ON author.bookId == book.bookId " +
            "LEFT JOIN email ON email.authorId == author.authorId"
    )
    fun getAll(): List<BookInfo>

    @Query("SELECT * FROM book WHERE bookId = :bookId")
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