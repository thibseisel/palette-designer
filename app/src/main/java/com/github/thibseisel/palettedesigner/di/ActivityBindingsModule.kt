package com.github.thibseisel.palettedesigner.di

import com.github.thibseisel.palettedesigner.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingsModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class, PictureSourceModule::class])
    abstract fun contributeMainActivity(): MainActivity
}