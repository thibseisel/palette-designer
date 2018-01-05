package com.github.thibseisel.palettedesigner.swatch

import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v7.graphics.Palette
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.glide.GlideApp
import com.github.thibseisel.palettedesigner.glide.PaletteBitmap
import com.github.thibseisel.palettedesigner.inflate
import com.github.thibseisel.palettedesigner.media.Picture

class PicturePagerAdapter(fragment: Fragment) : PagerAdapter() {

    private val pictures = ArrayList<Picture>()
    private val palettes = SparseArray<Palette>()

    private val glideCleaner = GlideApp.with(fragment)
    private val glideLoader = GlideApp.with(fragment)
            .`as`(PaletteBitmap::class.java)
            .centerCrop()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.inflate(R.layout.view_picture).also {
            container.addView(it)
            val pictureUri = pictures[position].uri
            val pictureView = it.findViewById<ImageView>(R.id.pictureView)

            glideLoader.load(pictureUri).into(object : ImageViewTarget<PaletteBitmap>(pictureView) {

                override fun setResource(resource: PaletteBitmap?) {
                    if (resource != null) {
                        super.view.setImageBitmap(resource.bitmap)
                        palettes.put(position, resource.palette)
                    }
                }
            })
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        with(obj as View) {
            val pictureView = findViewById<ImageView>(R.id.pictureView)
            glideCleaner.clear(pictureView)
            container.removeView(this)
            palettes.remove(position)
        }
    }

    override fun getCount() = pictures.size

    override fun isViewFromObject(view: View, obj: Any) = (view === obj)

    override fun getPageTitle(position: Int) = pictures[position].label

    fun paletteAtPosition(position: Int): Palette? = palettes[position]

    fun update(newUris: List<Picture>) {
        pictures.clear()
        pictures += newUris
        notifyDataSetChanged()
    }
}