package com.aberon.flexbook.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface StoreDao {

    @Transaction
    @Query(
        "SELECT * FROM book " +
                "LEFT JOIN bookParametr ON bookParametr.parametrOwnerId == book.bookId " +
                "LEFT JOIN covers ON covers.coverBookId == book.bookId " +
                "LEFT JOIN author ON author.ownedBookId == book.bookId " +
                "LEFT JOIN email ON email.authorId == author.authorId"
    )
    fun loadBooks(): List<BookInfo>

    @Transaction
    @Query(
        "SELECT book.bookId, book.language, book.title, book.description, book.type, book.path, " +
                "bookParametr.parametrId ,bookParametr.parametrOwnerId, bookParametr.parametrValue, " +
                "covers.coverId, covers.coverPath, " +
                "author.authorId, author.firstName, author.lastName, author.middleName, " +
                "author.nickname FROM book " +
                "LEFT JOIN bookParametr ON bookParametr.parametrOwnerId == book.bookId " +
                "LEFT JOIN covers ON covers.coverBookId == book.bookId " +
                "LEFT JOIN author ON author.ownedBookId == book.bookId " +
                "LEFT JOIN email ON email.authorId == author.authorId " +
                "WHERE book.bookId = :bookId"
    )
    fun loadById(bookId: String): BookInfo

    @Query("SELECT * FROM preference")
    fun loadPreferences(): List<Preference>

    @Insert
    fun insert(book: Book)

    @Insert
    fun insert(author: Author)

    @Insert
    fun insert(cover: Cover)

    @Insert
    fun insert(email: Email)

    @Insert
    fun insert(preference: Preference)

    @Insert
    fun insert(preference: BookParametr)

    @Insert
    fun insertAll(books: List<Book>)

    @Update
    fun update(preference: Preference)

    @Transaction
    @Update
    fun update(parametr: BookParametr)

    @Delete
    fun delete(book: Book)
}