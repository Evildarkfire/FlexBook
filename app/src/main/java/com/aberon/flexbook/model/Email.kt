package com.aberon.flexbook.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "email",
    foreignKeys = [
        ForeignKey(
            entity = Author::class,
            parentColumns = ["authorId"],
            childColumns = ["authorId"]
        ),
    ]
)
class Email(
    @PrimaryKey
    var emailId: String,
    var authorId: String,
    var email: String
)