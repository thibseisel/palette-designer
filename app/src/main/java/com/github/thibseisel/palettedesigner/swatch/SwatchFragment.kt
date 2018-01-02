package com.github.thibseisel.palettedesigner.swatch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
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

class SwatchFragment : Fragment() {

    @Inject lateinit var pictureSources: Map<Int, @JvmSuppressWildcards PictureSource>
    private lateinit var galleryAdapter: PicturePagerAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_swatch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryAdapter = PicturePagerAdapter(this)
        viewPager.adapter = galleryAdapter
        viewPager.offscreenPageLimit
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pictureSources.getValue(R.string.album_art_source).pictures.toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(galleryAdapter::update)
    }
}