package com.aberon.flexbook.model

import android.os.Parcel
import android.os.Parcelable
import java.lang.Exception

enum class BookType(val id: Int) : Parcelable {
    FB2(0);

    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookType> {
        override fun createFromParcel(parcel: Parcel): BookType {
            return when (parcel.readInt()) {
                0 -> BookType.FB2
                else -> throw Exception() //TODO parse error
            }
        }

        override fun newArray(size: Int): Array<BookType?> {
            return arrayOfNulls(size)
        }
    }
}