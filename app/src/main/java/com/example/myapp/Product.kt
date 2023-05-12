package com.example.myapp

import android.os.Parcel
import android.os.Parcelable

data class Product(
    var description: String?, var ingredients: String?,
    var name: String?, var prescription: Boolean, var price: String?,
    val sellerId: String?, var tags: String?
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(ingredients)
        parcel.writeString(price)
        parcel.writeString(tags)
        parcel.writeByte(if (prescription) 1 else 0)
        parcel.writeString(sellerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
