package com.example.assignmentindividu.items

import android.os.Parcel
import android.os.Parcelable


data class Flight(
    var flightID: Int?=null,
    var price: Int?=null,
    var airlineID: Int?=null,
    var airlineImage: String?=null,
    var airlineName: String?=null,
    var airplane: String?=null,
    var flightNumber: String?=null,
    var duration: Int?=null,
    var departureAirport: String?=null,
    var departureTime: String?=null,
    var departureId:String?=null,
    var arrivalAirport: String?=null,
    var arrivalTime: String?=null,
    var arrivalId:String?=null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(flightID!!)
        parcel.writeInt(price!!)
        parcel.writeInt(airlineID!!)
        parcel.writeString(airlineImage!!)
        parcel.writeString(airlineName!!)
        parcel.writeString(airplane!!)
        parcel.writeString(flightNumber!!)
        parcel.writeInt(duration!!)
        parcel.writeString(departureAirport!!)
        parcel.writeString(departureTime!!)
        parcel.writeString(departureId!!)
        parcel.writeString(arrivalAirport!!)
        parcel.writeString(arrivalTime!!)
        parcel.writeString(arrivalId!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flight> {
        override fun createFromParcel(parcel: Parcel): Flight {
            return Flight(parcel)
        }

        override fun newArray(size: Int): Array<Flight?> {
            return arrayOfNulls(size)
        }
    }
}
