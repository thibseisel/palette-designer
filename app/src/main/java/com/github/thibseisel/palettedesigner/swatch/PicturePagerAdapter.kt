package com.github.thibseisel.palettedesigner.swatch

import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.glide.GlideApp
import com.github.thibseisel.palettedesigner.inflate
import com.github.thibseisel.palettedesigner.media.Picture

class PicturePagerAdapter(private val fragment: Fragment) : PagerAdapter() {

    private val pictures = ArrayList<Picture>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.inflate(R.layout.view_picture).also {
            container.addView(it)
            val pictureUri = pictures[position].uri
            val pictureView = it.findViewById<ImageView>(R.id.pictureView)
            GlideApp.with(fragment).load(pictureUri)
                    .centerCrop()
                    .into(pictureView)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount() = pictures.size

    override fun isViewFromObject(view: View, obj: Any) = (view === obj)

    override fun getPageTitle(position: Int) = pictures[position].label

    fun update(newUris: List<Picture>) {
        pictures.clear()
        pictures += newUris
        notifyDataSetChanged()
    }
}