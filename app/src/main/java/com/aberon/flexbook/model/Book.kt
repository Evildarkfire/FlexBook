package com.aberon.flexbook.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class Book(
    @PrimaryKey
    var bookId: String,
    var title: String?,
    var description: String?,
    var type: BookType,
    var path: String
) {
    @Ignore
    var sections: List<Section>? = null

    @Ignore
    var parameters: Map<String, String>? = null
}