package com.aberon.flexbook.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable


class Book(
    val title: String?,
    val authors: List<Author>?,
    val description: String?,
    val type: BookType?,
    val covers: List<Bitmap>?,
    val path: String?,
    val sections: List<Section>?,
    val parameters: Map<String, String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Author),
        parcel.readString(),
        parcel.readParcelable(BookType::class.java.classLoader),
        parcel.createTypedArrayList(Bitmap.CREATOR),
        parcel.readString(),
        parcel.createTypedArrayList(Section),
        mutableMapOf<String, String>().apply {
            (0..parcel.readInt()).forEach { _ ->
                val key = parcel.readString()
                val value = parcel.readString()
                put(key ?: "", value ?: "")
            }
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(authors)
        parcel.writeString(description)
        parcel.writeParcelable(type, flags)
        parcel.writeTypedList(covers)
        parcel.writeString(path)
        parcel.writeTypedList(sections)

        parcel.writeInt(parameters!!.size)
        for ((key, value) in parameters.entries) {
            parcel.writeString(key)
            parcel.writeString(value)
        }
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