package com.github.thibseisel.palettedesigner.media

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.thibseisel.palettedesigner.R
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_media_chooser.*
import javax.inject.Inject

class MediaChooserFragment : Fragment() {

    @Inject lateinit var pictureSources: Map<Int, @JvmSuppressWildcards PictureSource>
    private lateinit var adapter: MediaChooserAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_media_chooser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MediaChooserAdapter(this, ::onPictureSelected)
        pictureRecycler.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pictureSources.getValue(R.string.album_art_source).pictures.toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onPicturesReady)
    }

    private fun onPicturesReady(pictures: List<Picture>) {
        adapter.updateWith(pictures)

        val startPosition = arguments?.getInt(ARG_POSITION) ?: 0
        pictureRecycler.scrollToPosition(startPosition)
    }

    private fun onPictureSelected(position: Int) {
        // Forward clicked picture position to the caller
        targetFragment?.let { caller ->
            fragmentManager!!.popBackStack()
            caller.onActivityResult(targetRequestCode, RESULT_OK, Intent().apply {
                putExtra(EXTRA_PICTURE_POSITION, position)
            })
        }
    }

    companion object Factory {

        private const val ARG_POSITION = "start_position"

        /**
         * Position of the selected picture in the grid.
         * This extra is forwarded to the caller's [onActivityResult].
         */
        const val EXTRA_PICTURE_POSITION = "picture_position"

        /**
         * Create a new instance of a fragment that promote selection of a picture in a grid.
         *
         * @param startPosition The position of an item to highlight in the grid.
         * @return new instance of MediaChooserFragment
         */
        fun newInstance(startPosition: Int) = MediaChooserFragment().apply {
            arguments = Bundle(1).apply {
                putInt(ARG_POSITION, startPosition)
            }
        }
    }
}