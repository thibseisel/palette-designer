package com.github.thibseisel.palettedesigner.di

import com.github.thibseisel.palettedesigner.swatch.SwatchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributeSwatchFragment(): SwatchFragment

}