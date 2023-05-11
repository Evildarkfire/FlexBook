package com.aberon.flexbook.model

import androidx.room.Embedded
import androidx.room.Relation

data class BookInfo(
    @Embedded
    var book: Book,
    @Relation(
        entity = Cover::class,
        parentColumn = "bookId",
        entityColumn = "coverBookId"
    )
    var covers: List<Cover>,
    @Relation(
        entity = Author::class,
        parentColumn = "bookId",
        entityColumn = "ownedBookId"
    )
    var authors: List<AuthorInfo>,
    @Relation(
        entity = BookParametr::class,
        parentColumn = "bookId",
        entityColumn = "parametrOwnerId"
    )
    var parametrs: List<BookParametr>
)