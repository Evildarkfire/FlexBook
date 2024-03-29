package com.aberon.flexbook.model

import android.graphics.Bitmap

class Section(
    val id: Int,
    val title: String?,
    val elements: List<Paragraph>?,
    val images: List<Bitmap>?,
    val parameters: List<String> = mutableListOf()
)