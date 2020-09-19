package com.example.currencyrates

import android.os.Parcel
import android.os.Parcelable

class CurrencyModel(
    var charCode: String?,
    var name: String?,
    var value: String?,
    var numCode: String?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(charCode)
        parcel.writeString(name)
        parcel.writeString(value)
        parcel.writeString(numCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyModel> {
        override fun createFromParcel(parcel: Parcel): CurrencyModel {
            return CurrencyModel(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyModel?> {
            return arrayOfNulls(size)
        }
    }

}