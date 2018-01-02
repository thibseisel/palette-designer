package com.github.thibseisel.palettedesigner.swatch

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.thibseisel.palettedesigner.media.Picture

class ImageGalleryAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val uris = ArrayList<Picture>()

    override fun getItem(position: Int): Fragment = PictureFragment.newInstance(uris[position])

    override fun getCount() = uris.size

    fun update(newUris: List<Picture>) {
        uris.clear()
        uris += newUris
        notifyDataSetChanged()
    }
}