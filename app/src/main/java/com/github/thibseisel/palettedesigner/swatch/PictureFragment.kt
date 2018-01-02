package com.github.thibseisel.palettedesigner.swatch

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.content.res.AppCompatResources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.glide.GlideApp
import com.github.thibseisel.palettedesigner.glide.GlideRequest
import com.github.thibseisel.palettedesigner.media.Picture
import kotlinx.android.synthetic.main.fragment_picture.*

class PictureFragment : Fragment() {

    private lateinit var glide: GlideRequest<Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = checkNotNull(context) { "Fragment is not attached" }

        glide = GlideApp.with(this).asBitmap()
                .fallback(AppCompatResources.getDrawable(context, R.drawable.ic_image_24dp))
                .error(AppCompatResources.getDrawable(context, R.drawable.ic_broken_image_24dp))
                .centerCrop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_picture, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getParcelable<Picture>(PARAM_PICTURE)?.let { picture ->
            glide.load(picture.uri).into(pictureView)
        } ?: throw IllegalStateException("Fragment should be created with newInstance")
    }

    companion object Factory {
        private const val PARAM_PICTURE = "picture"

        fun newInstance(pic: Picture) = PictureFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(PARAM_PICTURE, pic)
            }
        }
    }
}
