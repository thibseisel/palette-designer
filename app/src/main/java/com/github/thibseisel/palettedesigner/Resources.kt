package com.github.thibseisel.palettedesigner

import android.content.Context
import android.support.annotation.Dimension
import android.support.annotation.LayoutRes
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Inflate a new view hierarchy from XML as this view children.
 *
 * @receiver The view that will be the parent for the inflated view hierarchy.
 * @param resource ID for an XML layout resource to load.
 * @param attachToRoot Whether the inflated view hierarchy should be attached to its parent.
 * If `false`, root is only used to create the correct LayoutParams subclass.
 * @return The root View of the inflated hierarchy. If `attachToRoot` is `true`, it is `this`,
 * otherwise it is the root of the inflated XML file.
 */
fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)

/**
 * Produce a string representation of an opaque color.
 *
 * @receiver A color as a RGB-packed integer.
 * @return A String representation of this color in the format `#RRGGBB`.
 */
fun Int.toColorHex() = String.format("#%06X", this and 0xFFFFFF)

/**
 * Convert a given amount of DP to its equivalent number of pixels.
 *
 * @param context The context of this application to retrieve device metrics
 * @param dp The amount of DP units to convert
 * @return The equivalent amount of DP as pixels
 */
fun dpToPixels(context: Context, @Dimension(unit = Dimension.DP) dp: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
}