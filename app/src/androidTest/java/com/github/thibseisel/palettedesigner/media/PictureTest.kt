package com.github.thibseisel.palettedesigner.media

import android.net.Uri
import android.support.test.runner.AndroidJUnit4
import com.github.thibseisel.palettedesigner.parcelReadWrite
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PictureTest {

    @Test
    fun isParcelable() {
        val subject = Picture(
                label = "My picture",
                uri = Uri.parse("https://my.domain.com/pictures/mypicture.png")
        )

        val parceled = parcelReadWrite(subject)
        assertEquals(subject.label, parceled.label)
        assertEquals(subject.uri, parceled.uri)
    }

}