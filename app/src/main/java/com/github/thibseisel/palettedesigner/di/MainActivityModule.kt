package com.github.thibseisel.palettedesigner.di

import com.github.thibseisel.palettedesigner.media.MediaChooserFragment
import com.github.thibseisel.palettedesigner.swatch.SwatchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSwatchFragment(): SwatchFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeMediaChooserFragment(): MediaChooserFragment

}