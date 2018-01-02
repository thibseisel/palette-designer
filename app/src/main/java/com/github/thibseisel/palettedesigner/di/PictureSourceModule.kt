package com.github.thibseisel.palettedesigner.di

import com.github.thibseisel.palettedesigner.media.AlbumArtPictureSource
import com.github.thibseisel.palettedesigner.R
import com.github.thibseisel.palettedesigner.media.PictureSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap

@Module
abstract class PictureSourceModule {

    @Binds
    @IntoMap
    @IntKey(R.string.album_art_source)
    abstract fun BindsAlbumArtSource(source: AlbumArtPictureSource): PictureSource
}