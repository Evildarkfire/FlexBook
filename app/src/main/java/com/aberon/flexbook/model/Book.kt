package com.aberon.flexbook.model

import android.graphics.Bitmap

class Book(
    val title: String,
    val authors: List<Author>,
    val description: String,
    val type: BookType,
    val image: Bitmap?,
    val path: String,
    val sections: List<Section>?
)