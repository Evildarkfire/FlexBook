package com.aberon.flexbook.model

import android.os.Parcel
import android.os.Parcelable

interface Element : Parcelable {
    object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(p0: Parcel?): Element {
            TODO("Not yet implemented")
        }

        override fun newArray(p0: Int): Array<Element> {
            TODO("Not yet implemented")
        }
    }

    val parameters: List<String>?
}