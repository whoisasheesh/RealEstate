package com.whoisasheesh.realestate.data

import android.os.Parcel
import android.os.Parcelable

data class Property(
    var propertyId: String?,
    var bedroom: String?,
    var bathroom: String?,
    var carspace: String?,
    var propertyPrice: String?,
    var currencyType: String?,
    var propertyType: String?,
    var saleType: String?,
    var agentInfo: String?,
    var ownerInfo: String?,
    var address: String?,
    var suburb: String?,
    var postcode: String?,
    var state: String?,
    var agentImg: String?,
    var propertyImg: String?,
    var ownerImg: String?,
    var propertyDesc: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(propertyId)
        parcel.writeString(bedroom)
        parcel.writeString(bathroom)
        parcel.writeString(carspace)
        parcel.writeString(propertyPrice)
        parcel.writeString(currencyType)
        parcel.writeString(propertyType)
        parcel.writeString(saleType)
        parcel.writeString(agentInfo)
        parcel.writeString(ownerInfo)
        parcel.writeString(address)
        parcel.writeString(suburb)
        parcel.writeString(postcode)
        parcel.writeString(state)
        parcel.writeString(agentImg)
        parcel.writeString(propertyImg)
        parcel.writeString(ownerImg)
        parcel.writeString(propertyDesc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Property> {
        override fun createFromParcel(parcel: Parcel): Property {
            return Property(parcel)
        }

        override fun newArray(size: Int): Array<Property?> {
            return arrayOfNulls(size)
        }
    }
}
