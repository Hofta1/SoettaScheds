package com.example.assignmentindividu.items

import android.os.Parcel
import android.os.Parcelable

class Doll(
    var path: String?=null,
    var id: Int?=null,
    var name: String?=null,
    var size: String?=null,
    var rating: Double?=null,
    var price: Double?=null,
    var description: String?=null
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path!!)
        parcel.writeInt(id!!)
        parcel.writeString(name)
        parcel.writeString(size!!)
        parcel.writeDouble(rating!!)
        parcel.writeDouble(price!!)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doll> {
        override fun createFromParcel(parcel: Parcel): Doll {
            return Doll(parcel)
        }

        override fun newArray(size: Int): Array<Doll?> {
            return arrayOfNulls(size)
        }
    }
}