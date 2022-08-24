package com.aberon.flexbook.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Book(
    val title: String?,
    val authors: List<Author>?,
    val description: String?,
    val type: BookType?,
    val image: Bitmap?,
    val path: String?,
    val sections: List<Section>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Author),
        parcel.readString(),
        parcel.readParcelable(BookType::class.java.classLoader),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.createTypedArrayList(Section)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(authors)
        parcel.writeString(description)
        parcel.writeParcelable(type, flags)
        parcel.writeParcelable(image, flags)
        parcel.writeString(path)
        parcel.writeTypedList(sections)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}