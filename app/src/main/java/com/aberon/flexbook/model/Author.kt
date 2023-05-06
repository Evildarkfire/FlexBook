package com.aberon.flexbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "author",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["bookId"],
            childColumns = ["ownedBookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Author(
    @PrimaryKey
    var authorId: String,
    @ColumnInfo(index = true)
    var ownedBookId: String,
    var lastName: String?,
    var middleName: String?,
    var firstName: String?,
    var nickname: String?
) {
    val fullName: String?
        get() {
            return if (lastName != null && firstName != null && middleName != null) {
                "$lastName $firstName $middleName"
            } else if (lastName != null && firstName != null) {
                "$lastName $firstName"
            } else lastName
        }
}