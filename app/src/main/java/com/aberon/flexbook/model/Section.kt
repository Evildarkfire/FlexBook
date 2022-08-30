package com.aberon.flexbook.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Section(
    val title: String?,
    val annotation: List<Element>?,
    val elements: List<Paragraph>?,
    val epigraph: List<Element>?,
    val image: Bitmap?,
    override val parameters: List<String>?
) : Element {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(Element.CREATOR),
        parcel.createTypedArrayList(Paragraph.CREATOR),
        parcel.createTypedArrayList(Element.CREATOR),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(annotation)
        parcel.writeTypedList(elements)
        parcel.writeTypedList(epigraph)
        parcel.writeParcelable(image, flags)
        parcel.writeStringList(parameters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Section> {
        override fun createFromParcel(parcel: Parcel): Section {
            return Section(parcel)
        }

        override fun newArray(size: Int): Array<Section?> {
            return arrayOfNulls(size)
        }
    }
}