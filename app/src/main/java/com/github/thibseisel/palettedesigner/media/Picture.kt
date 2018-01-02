package com.github.thibseisel.palettedesigner.media

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Information on a picture to be displayed.
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Picture(val label: CharSequence, val uri: Uri) : Parcelable