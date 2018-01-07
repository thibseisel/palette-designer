package com.github.thibseisel.palettedesigner.swatch

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.media.MediaChooserFragment
import com.github.thibseisel.palettedesigner.media.PictureSource
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_swatch.*
import javax.inject.Inject

class SwatchFragment : Fragment(), ViewPager.OnPageChangeListener {

    private companion object {
        private const val REQUEST_CHOOSE_PICTURE = 99
    }

    @Inject lateinit var pictureSources: Map<Int, @JvmSuppressWildcards PictureSource>

    private lateinit var picturesAdapter: PicturePagerAdapter
    private lateinit var swatchAdapter: SwatchAdapter

    private val handler = Handler()

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        picturesAdapter = PicturePagerAdapter(this, ::onPaletteGenerated)
        swatchAdapter = SwatchAdapter(::onSwatchSelected)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_swatch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(picturePager) {
            adapter = picturesAdapter
            addOnPageChangeListener(this@SwatchFragment)
            setOnClickListener { openPictureChooser() }
        }

        swatchRecycler.adapter = swatchAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pictureSources.getValue(R.string.album_art_source).pictures.toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(picturesAdapter::update)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CHOOSE_PICTURE && resultCode == RESULT_OK) {
            // A picture has been picked in the picture chooser
            val position = data?.getIntExtra(MediaChooserFragment.EXTRA_PICTURE_POSITION, 0) ?: 0
            onPictureSelectedFromChooser(position)
        }
    }

    /**
     * Open the picture chooser.
     */
    private fun openPictureChooser() {
        val chooserFragment = MediaChooserFragment.newInstance(picturePager.currentItem)
        chooserFragment.setTargetFragment(this, REQUEST_CHOOSE_PICTURE)

        fragmentManager!!.beginTransaction()
                .replace(R.id.container, chooserFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    private fun onPictureSelectedFromChooser(position: Int) {
        // Post the action so that the fragment can instantiate its views before handling action
        handler.post { picturePager.currentItem = position }
    }

    /**
     * Called when a color palette has been generated for the picture at the given position.
     *
     * @param position Position of the picture whose palette has been generated in the adapter.
     */
    private fun onPaletteGenerated(position: Int, palette: Palette) {
        if (position == picturePager.currentItem) {
            updateSwatchesFromPalette(palette)
        }
    }

    /**
     * Called when a swatch associated with the currently displayed picture
     * has been selected.
     *
     * @param position Position of the selected swatch in the list backed by [swatchAdapter].
     */
    private fun onSwatchSelected(position: Int) {
        // TODO Activity transition
    }

    override fun onPageScrollStateChanged(state: Int) {
        // Do nothing.
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        // Do nothing.
    }

    override fun onPageSelected(position: Int) {
        picturesAdapter.paletteAtPosition(position)?.let(::updateSwatchesFromPalette)
    }

    private fun updateSwatchesFromPalette(palette: Palette) {
        swatchAdapter.updateWith(listOf(
                LabeledSwatch("Dominant", palette.dominantSwatch),
                LabeledSwatch("Light Vibrant", palette.lightVibrantSwatch),
                LabeledSwatch("Vibrant", palette.vibrantSwatch),
                LabeledSwatch("Dark Vibrant", palette.darkVibrantSwatch),
                LabeledSwatch("Light Muted", palette.lightMutedSwatch),
                LabeledSwatch("Muted", palette.mutedSwatch),
                LabeledSwatch("Dark Muted", palette.darkMutedSwatch)
        ))
    }
}