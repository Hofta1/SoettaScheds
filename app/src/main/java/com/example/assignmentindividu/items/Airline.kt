package com.example.assignmentindividu.items

import android.os.Parcel
import android.os.Parcelable

class Airline(
    var path: String?=null,
    var id: Int?=null,
    var name: String?=null,
    var airplane: String?=null,
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path!!)
        parcel.writeInt(id!!)
        parcel.writeString(name)
        parcel.writeString(airplane)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Airline> {
        override fun createFromParcel(parcel: Parcel): Airline {
            return Airline(parcel)
        }

        override fun newArray(size: Int): Array<Airline?> {
            return arrayOfNulls(size)
        }
    }
}