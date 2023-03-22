package com.aberon.flexbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "email",
    foreignKeys = [
        ForeignKey(
            entity = Author::class,
            parentColumns = ["authorId"],
            childColumns = ["authorId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
class Email(
    @PrimaryKey
    var emailId: String,
    @ColumnInfo(index = true)
    var authorId: String,
    var email: String
)