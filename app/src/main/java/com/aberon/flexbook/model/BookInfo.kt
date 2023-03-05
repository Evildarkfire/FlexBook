package com.aberon.flexbook.model

import androidx.room.Embedded
import androidx.room.Relation

data class BookInfo(
    @Embedded
    var book: Book,
    @Relation(
        entity = Cover::class,
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    var covers: List<Cover>,
    @Relation(
        entity = Author::class,
        parentColumn = "bookId",
        entityColumn = "bookId"
    )
    var authors: List<AuthorInfo>
)