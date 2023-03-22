package com.aberon.flexbook.model

import android.os.Parcel
import android.os.Parcelable
import java.lang.Exception

enum class BookType(val id: Int) {
    FB2(0),
    EPUB(1)
}