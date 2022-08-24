package com.aberon.flexbook.model

import android.os.Parcel
import android.os.Parcelable

class Author(
    val lastName: String?,
    val middleName: String?,
    val firstName: String?,
    val fullName: String?,
    val nickname: String?,
    val emails: List<String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lastName)
        parcel.writeString(middleName)
        parcel.writeString(firstName)
        parcel.writeString(fullName)
        parcel.writeString(nickname)
        parcel.writeStringList(emails)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Author> {
        override fun createFromParcel(parcel: Parcel): Author {
            return Author(parcel)
        }

        override fun newArray(size: Int): Array<Author?> {
            return arrayOfNulls(size)
        }
    }
}