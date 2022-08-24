package com.aberon.flexbook.model

import android.graphics.Bitmap

class Section(
    val title: String?,
    val annotation: List<Element>?,
    val elements: List<Element>?,
    val epigraph: List<Element>?,
    val image: Bitmap?,
    override val parameters: List<String>
): Element