package com.github.thibseisel.palettedesigner.media

import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.glide.GlideApp
import com.github.thibseisel.palettedesigner.glide.GlideRequest
import com.github.thibseisel.palettedesigner.inflate

class MediaChooserAdapter(
        fragment: Fragment,
        private val selectionListener: (Int) -> Unit
) : RecyclerView.Adapter<MediaChooserAdapter.MediaHolder>() {

    private val items = ArrayList<Picture>()
    private val glide = GlideApp.with(fragment).asBitmap()
            .thumbnail(0.2f)
            .transition(withCrossFade())
            .centerCrop()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaHolder {
        return MediaHolder(parent, glide).also {
            it.attachListeners(selectionListener)
        }
    }

    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        holder.bind(items[position])
    }

    operator fun get(position: Int): Picture {
        if (position in items.indices) {
            return items[position]
        } else throw IndexOutOfBoundsException()
    }

    fun updateWith(newPictures: List<Picture>) {
        items.clear()
        items += newPictures
        notifyDataSetChanged()
    }

    class MediaHolder(
            parent: ViewGroup,
            private val glide: GlideRequest<Bitmap>
    ) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_media)) {
        private val mediaView = itemView.findViewById<ImageView>(R.id.mediaView)

        fun attachListeners(listener: (Int) -> Unit) {
            itemView.setOnClickListener { _ ->
                listener(adapterPosition)
            }
        }

        fun bind(picture: Picture) {
            glide.load(picture.uri).into(mediaView)
        }
    }
}