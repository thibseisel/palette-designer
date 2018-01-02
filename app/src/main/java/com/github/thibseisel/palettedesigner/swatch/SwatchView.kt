package com.github.thibseisel.palettedesigner.swatch

import android.content.Context
import android.graphics.Canvas
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import android.view.View

class SwatchView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        // TODO Initialization of Paint
    }

    fun setColorSwatch(swatch: Palette.Swatch?) {
        // TODO Change Paint properties to reflect the swatch
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // TODO Actual drawing
    }

    private fun drawCheckerboard(canvas: Canvas) {
        // TODO Draw a checkerboard when no color is available in the swatch
    }
}