package com.github.thibseisel.ratioimageview

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.widget.ImageView
import kotlin.math.roundToInt

/**
 * An ImageView whose dimensions can be constrained by an aspect ratio.
 * Ratio is only applied if this View does not extend beyond its parent bounds.
 */
class RatioImageView
@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        const val RATIO_SIDE_WIDTH = 0
        const val RATIO_SIDE_HEIGHT = 1
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [RATIO_SIDE_WIDTH.toLong(), RATIO_SIDE_HEIGHT.toLong()])
    annotation class AspectRatioSide

    @AspectRatioSide
    private var ratioSide: Int = RATIO_SIDE_WIDTH

    private var aspectRatio: Float = Float.NaN

    init {
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RatioImageView,
                    defStyleAttr, 0)
            try {
                val stringRatio = a.getString(R.styleable.RatioImageView_aspectRatio)
                aspectRatio = parseRatio(stringRatio)
                ratioSide = a.getInt(R.styleable.RatioImageView_aspectRatioSide, RATIO_SIDE_WIDTH)
            } finally {
                a.recycle()
            }
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (aspectRatio.isNaN()) {
            // Aspect ratio is disabled.
            return
        }

        val newMeasuredWidth: Int
        val newMeasuredHeight: Int

        if (ratioSide == RATIO_SIDE_HEIGHT) {

            newMeasuredHeight = measuredHeight
            val desiredMeasuredWidth = (newMeasuredHeight / aspectRatio).roundToInt()

            // Ratio is respected only if it satisfies the widthMeasureSpec
            newMeasuredWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
                MeasureSpec.UNSPECIFIED -> desiredMeasuredWidth
                else -> minOf(desiredMeasuredWidth, MeasureSpec.getSize(widthMeasureSpec))
            }

        } else {
            newMeasuredWidth = measuredWidth
            val desiredMeasuredHeight = (newMeasuredWidth / aspectRatio).roundToInt()

            // Ratio is respected only if it satisfies the heightMeasureSpec
            newMeasuredHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.UNSPECIFIED -> desiredMeasuredHeight
                else -> minOf(desiredMeasuredHeight, MeasureSpec.getSize(heightMeasureSpec))
            }
        }

        setMeasuredDimension(newMeasuredWidth, newMeasuredHeight)
    }

    fun setAspectRatio(ratio: Float) {
        aspectRatio = if (ratio.isFinite() && ratio > 0f) ratio else Float.NaN
        requestLayout()
    }

    fun setAspectRatio(dimensionRatio: String?) {
        aspectRatio = parseRatio(dimensionRatio)
        requestLayout()
    }

    fun setAspectRatioSide(@AspectRatioSide side: Int) {
        ratioSide = if (side != RATIO_SIDE_HEIGHT) RATIO_SIDE_WIDTH else side
        requestLayout()
    }
}