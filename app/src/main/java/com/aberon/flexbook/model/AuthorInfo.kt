package com.aberon.flexbook.model

import androidx.room.Embedded
import androidx.room.Relation

data class AuthorInfo(
    @Embedded
    var author: Author,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "authorId"
    )
    var emails: List<Email>
)