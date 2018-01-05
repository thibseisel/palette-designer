package com.github.thibseisel.palettedesigner.swatch

import android.content.Context
import android.graphics.*
import android.support.v4.view.ViewCompat
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.dpToPixels
import com.github.thibseisel.palettedesigner.toColorHex
import kotlin.math.roundToInt

private const val DEFAULT_CHECKERBOARD_COLOR: Int = 0xFFE0E0E0.toInt()
private const val DEFAULT_SQUARE_SIZE_DP = 8f
private const val TEXT_PADDING_DP = 16f

class SwatchView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var hasColor = false
    private var swatchColor: Int = 0

    // Label to be displayed in the top-left corner
    private var label: String? = null
    private var labelX = 0f
    private var labelY = 0f
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24f,
                context.resources.displayMetrics)
        color = Color.BLACK
    }

    // String representation of the color to be displayed on the bottom-right

    private var colorHex: String? = null
    private var hexX = 0f
    private var hexY = 0f
    private val colorHexPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f,
                context.resources.displayMetrics)
        color = Color.BLACK
    }

    private val textPadding = dpToPixels(context, TEXT_PADDING_DP).roundToInt()

    private val boardRect = Rect()
    private val boardPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private fun configureCheckerboard(color: Int, squareSize: Int) {
        // Create the checkerboard pattern to be repeated
        val side = 2 * squareSize
        val template = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
        for (x in 0 until side) {
            for (y in 0 until side) {
                val checkerX = x / (squareSize)
                val checkerY = y / (squareSize)
                template.setPixel(x, y,
                        if ((checkerX + checkerY) % 2 == 0) color else Color.TRANSPARENT)
            }
        }

        // Use a shader to repeat the checkerboard pattern
        boardPaint.shader = BitmapShader(template,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    }

    init {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SwatchView,
                    defStyleAttr, 0)
            try {
                label = a.getString(R.styleable.SwatchView_swatchLabel)
                val boardColor = a.getColor(R.styleable.SwatchView_checkerboardColor,
                        DEFAULT_CHECKERBOARD_COLOR)
                val squareSize = a.getDimensionPixelSize(R.styleable.SwatchView_squareSize,
                        dpToPixels(context, DEFAULT_SQUARE_SIZE_DP).roundToInt())
                configureCheckerboard(boardColor, squareSize)

                if (a.hasValue(R.styleable.SwatchView_swatchColor)) {
                    val color = a.getColor(R.styleable.SwatchView_swatchColor, 0)
                    val swatch = Palette.Swatch(color, 0)
                    setSwatch(swatch)
                }

            } finally {
                a.recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        measureLabelAndHex(w, h)
        boardRect.right = w
        boardRect.bottom = h
    }

    private fun measureLabelAndHex(w: Int, h: Int) {
        // A rectangle to hold text bounds
        val bounds = Rect()

        // Label is displayed in the top-start corner padded by 8dp
        labelPaint.getTextBounds(label.orEmpty(), 0, label?.length ?: 0, bounds)
        labelX = (ViewCompat.getPaddingStart(this) + textPadding).toFloat()
        labelY = (paddingTop + textPadding - bounds.top).toFloat()

        // colorHex is displayed in the bottom-end corner padded by 8dp
        // Calculate text bounds to subtract them as the text is right aligned.
        colorHexPaint.getTextBounds(colorHex.orEmpty(), 0, colorHex?.length ?: 0, bounds)
        hexX = (w - ViewCompat.getPaddingEnd(this) - textPadding - bounds.width()).toFloat()
        hexY = (h - paddingBottom - textPadding - bounds.bottom).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        if (hasColor) canvas.drawColor(swatchColor) else canvas.drawRect(boardRect, boardPaint)

        if (label != null) canvas.drawText(label, labelX, labelY, labelPaint)
        if (colorHex != null) canvas.drawText(colorHex, hexX, hexY, colorHexPaint)
    }

    fun setLabel(label: String) {
        this.label = label
        invalidate()
    }

    fun setSwatch(swatch: Palette.Swatch?) {
        if (swatch != null) {
            hasColor = true
            swatchColor = swatch.rgb
            colorHex = swatchColor.toColorHex()
            labelPaint.color = swatch.titleTextColor
            colorHexPaint.color = swatch.bodyTextColor
        } else {
            hasColor = false
            swatchColor = 0
            colorHex = null
            labelPaint.color = Color.BLACK
            colorHexPaint.color = Color.BLACK
        }

        invalidate()
    }
}