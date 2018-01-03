package com.github.thibseisel.palettedesigner.swatch

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.graphics.Palette

data class LabeledSwatch(val label: String, val swatch: Palette.Swatch?) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        if (swatch != null) {
            parcel.writeInt(swatch.rgb)
            parcel.writeInt(swatch.population)
        }
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<LabeledSwatch> {

        override fun createFromParcel(parcel: Parcel): LabeledSwatch {
            val label = parcel.readString()

            val swatch = if (parcel.dataAvail() > 0) {
                val color = parcel.readInt()
                val population = parcel.readInt()
                Palette.Swatch(color, population)
            } else null

            return LabeledSwatch(label, swatch)
        }

        override fun newArray(size: Int): Array<LabeledSwatch?> {
            return arrayOfNulls(size)
        }
    }
}