package com.github.thibseisel.palettedesigner.media

import io.reactivex.Observable

/**
 * A data source providing pictures.
 * Implementations decide the kind of picture and where they come from.
 */
interface PictureSource {

    /**
     * An observable stream of pictures.
     */
    val pictures: Observable<Picture>
}