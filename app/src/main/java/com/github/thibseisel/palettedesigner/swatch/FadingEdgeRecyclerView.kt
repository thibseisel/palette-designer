package com.github.thibseisel.palettedesigner.swatch

import android.content.Context
import android.graphics.*
import android.support.annotation.Px
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.github.thibseisel.palettedesigner.R

class FadingEdgeRecyclerView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val viewBounds = RectF()

    private val fadingEdgeRect = Rect()
    private val fadingEdgePaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    /**
     * The length of the fading edge on top of this view in pixels.
     */
    @Px
    var edgeLength: Int = 0
        set(value) {
            field = value
            configureEdge(value)
        }

    init {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs,
                    R.styleable.FadingEdgeRecyclerView, 0, 0)
            try {
                edgeLength = a.getDimensionPixelSize(
                        R.styleable.FadingEdgeRecyclerView_fadingEdgeLength, 0)
            } finally {
                a.recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        viewBounds.set(0f, 0f, width.toFloat(), height.toFloat())

        if (h != oldh) {
            configureEdge(edgeLength)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (visibility == View.GONE || edgeLength == 0) {
            super.dispatchDraw(canvas)
            return
        }

        val count = canvas.saveLayer(viewBounds, null, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        canvas.drawRect(fadingEdgeRect, fadingEdgePaint)
        canvas.restoreToCount(count)
    }

    private fun configureEdge(@Px length: Int) {
        if (length == 0) {
            fadingEdgeRect.setEmpty()
            fadingEdgePaint.shader = null
        } else {
            val actualHeight = paddingTop + height + paddingBottom
            val size = minOf(length, actualHeight)

            val left = ViewCompat.getPaddingStart(this)
            val top = 0
            val right = ViewCompat.getPaddingEnd(this)
            val bottom = top + size

            fadingEdgeRect.set(left, top, right, bottom)
            fadingEdgePaint.shader = LinearGradient(
                    left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    Color.TRANSPARENT, Color.WHITE,
                    Shader.TileMode.CLAMP
            )
        }
    }
}