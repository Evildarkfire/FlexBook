package com.aberon.flexbook.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "author",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Author(
    @PrimaryKey
    var authorId: String,
    var bookId: String,
    var lastName: String?,
    var middleName: String?,
    var firstName: String?,
    var fullName: String?,
    var nickname: String?
)