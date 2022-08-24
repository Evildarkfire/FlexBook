package com.aberon.flexbook.model

import android.os.Parcel
import android.os.Parcelable

class Paragraph(
    override val parameters: List<String>?,
    val text: String?
) : Element {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(parameters)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Paragraph> {
        override fun createFromParcel(parcel: Parcel): Paragraph {
            return Paragraph(parcel)
        }

        override fun newArray(size: Int): Array<Paragraph?> {
            return arrayOfNulls(size)
        }
    }
}