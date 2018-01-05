package com.github.thibseisel.palettedesigner.swatch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.media.PictureSource
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_swatch.*
import javax.inject.Inject

private const val TAG = "SwatchFragment"

class SwatchFragment : Fragment(), ViewPager.OnPageChangeListener {

    @Inject lateinit var pictureSources: Map<Int, @JvmSuppressWildcards PictureSource>
    private lateinit var picturesAdapter: PicturePagerAdapter

    private lateinit var swatchAdapter: SwatchAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_swatch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        picturesAdapter = PicturePagerAdapter(this)
        picturePager.adapter = picturesAdapter
        picturePager.addOnPageChangeListener(this)

        swatchAdapter = SwatchAdapter { position ->
            Log.d(TAG, "Selected swatch at position = $position")
            val pickedSwatch = swatchAdapter[position]
            val holder = swatchRecycler.findViewHolderForAdapterPosition(position)
            // TODO Activity transition
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

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        picturesAdapter.paletteAtPosition(position)?.let(this::updateSwatchesFromPalette)
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