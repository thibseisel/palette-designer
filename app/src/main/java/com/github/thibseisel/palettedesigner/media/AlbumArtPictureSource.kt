package com.github.thibseisel.palettedesigner.media

import android.Manifest
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Audio.Albums
import com.github.thibseisel.palettedesigner.checkPermissionIsGranted
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AlbumArtPictureSource
@Inject constructor(
        private val context: Context
) : PictureSource {

    override val pictures: Observable<Picture> = Observable.create<Picture> { emitter ->
        context.checkPermissionIsGranted(Manifest.permission.READ_EXTERNAL_STORAGE)

        context.contentResolver.query(Albums.EXTERNAL_CONTENT_URI,
                arrayOf(Albums.ALBUM, Albums.ALBUM_ART), null, null,
                Albums.DEFAULT_SORT_ORDER)?.use { cursor ->

            val colLabel = cursor.getColumnIndexOrThrow(Albums.ALBUM)
            val colFilepath = cursor.getColumnIndexOrThrow(Albums.ALBUM_ART)

            while (cursor.moveToNext() && !emitter.isDisposed) {
                cursor.getString(colFilepath)?.let {
                    val artUri = Uri.parse("file://$it")
                    val albumArt = Picture(cursor.getString(colLabel), artUri)
                    emitter.onNext(albumArt)
                }
            }

        } ?: throw AssertionError("Query failed: null cursor")
        emitter.onComplete()
    }
            .cache()
            .subscribeOn(Schedulers.io())
}