package com.github.thibseisel.palettedesigner

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler

/**
 * Write an object to an Android Parcel then read it back using the provided creator.
 *
 * @param value Object to write to the parcel.
 * @param creator The parcelable creator associated with the written object's class.
 * @return a copy of the written object read from the same parcel.
 */
fun <P : Parcelable> parcelReadWrite(value: P, creator: Parcelable.Creator<P>): P {
    return with(Parcel.obtain()) {
        value.writeToParcel(this, 0)
        setDataPosition(0)
        creator.createFromParcel(this).also {
            recycle()
        }
    }
}

/**
 * Write an object to an Android Parcel then read it back.
 *
 * @param P The type of the parcelable object to write.
 * @param value Object to write to the parcel.
 * @return a copy of the written object read from the same parcel.
 */
inline fun <reified P : Parcelable> parcelReadWrite(value: P): P {
    return with(Parcel.obtain()) {
        writeParcelable(value, 0)
        setDataPosition(0)
        readParcelable<P>(P::class.java.classLoader).also {
            recycle()
        }
    }
}