package com.aberon.flexbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "covers",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["bookId"],
            childColumns = ["coverBookId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
class Cover(
    @PrimaryKey
    var coverId: String,
    @ColumnInfo(index = true)
    var coverBookId: String,
    var coverPath: String
) {
    @Ignore
    var coverBytes: ByteArray? = null
}