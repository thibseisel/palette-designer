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

/**
 * An adapter to display a set of pictures from which color palettes should be generated.
 *
 * @param fragment The fragment associated with this adapter
 * @param onPaletteGenerated A function to be called when a palette has been generated.
 * Its parameters are the position of the picture that generated the palette,
 * and the generated palette itself.
 */
class PicturePagerAdapter(
        fragment: Fragment,
        private val onPaletteGenerated: (Int, Palette) -> Unit
) : PagerAdapter() {

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

            // Forward clicks to items to the ViewPager parent
            it.setOnClickListener { _ ->
                container.performClick()
            }

            glideLoader.load(pictureUri).into(object : ImageViewTarget<PaletteBitmap>(pictureView) {

                override fun setResource(resource: PaletteBitmap?) {
                    if (resource != null) {
                        super.view.setImageBitmap(resource.bitmap)
                        palettes.put(position, resource.palette)
                        onPaletteGenerated(position, resource.palette)
                    }
                }
            })
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        with(obj as View) {
            // Free Glide resources for this view
            val pictureView = findViewById<ImageView>(R.id.pictureView)
            glideCleaner.clear(pictureView)
            // Remove listeners
            setOnClickListener(null)
            // Detach this view from its parent
            container.removeView(this)
            // Clear the generated palette for this view
            palettes.remove(position)
        }
    }

    override fun getCount() = pictures.size

    override fun isViewFromObject(view: View, obj: Any) = (view === obj)

    override fun getPageTitle(position: Int): CharSequence {
        if (position in pictures.indices) {
            return pictures[position].label
        } else throw IndexOutOfBoundsException()
    }

    /**
     * Retrieve the color palette generated for a picture at the given position.
     *
     * @param position Position of the picture from which the palette has been generated.
     * @return The palette for the given position,
     * or `null` if no palette has been generate for this position.
     */
    fun paletteAtPosition(position: Int): Palette? = palettes[position]

    /**
     * Replace the set of displayed pictures with the provided one.
     */
    fun update(newUris: List<Picture>) {
        pictures.clear()
        pictures += newUris
        notifyDataSetChanged()
    }
}