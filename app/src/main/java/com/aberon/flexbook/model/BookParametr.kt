package com.aberon.flexbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "bookParametr",
    primaryKeys = [
        "parametrId",
        "parametrOwnerId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["bookId"],
            childColumns = ["parametrOwnerId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
class BookParametr(
    var parametrId: BookParametrKey,
    @ColumnInfo(index = true)
    var parametrOwnerId: String,
    var parametrValue: Int
)