package com.github.thibseisel.palettedesigner.glide

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.util.Util

class PaletteBitmapTranscoder(
        private val bitmapPool: BitmapPool
) : ResourceTranscoder<Bitmap, PaletteBitmap> {

    override fun transcode(toTranscode: Resource<Bitmap>, options: Options): Resource<PaletteBitmap> {
        val bitmap = toTranscode.get()
        val palette = onGeneratePalette(bitmap)
        val result = PaletteBitmap(palette, bitmap)
        return PaletteBitmapResource(result, bitmapPool)
    }

    private fun onGeneratePalette(bitmap: Bitmap): Palette {
        return Palette.from(bitmap).generate()
    }
}

data class PaletteBitmap(val palette: Palette, val bitmap: Bitmap)

internal class PaletteBitmapResource(
        private val paletteBitmap: PaletteBitmap,
        private val bitmapPool: BitmapPool
) : Resource<PaletteBitmap> {

    override fun get() = paletteBitmap

    override fun getResourceClass() = PaletteBitmap::class.java

    @SuppressLint("NewApi")
    override fun getSize() = Util.getBitmapByteSize(paletteBitmap.bitmap)

    override fun recycle() = bitmapPool.put(paletteBitmap.bitmap)

}