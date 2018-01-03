package com.github.thibseisel.palettedesigner.swatch

import android.support.test.runner.AndroidJUnit4
import android.support.v7.graphics.Palette
import com.github.thibseisel.palettedesigner.parcelReadWrite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LabeledSwatchTest {

    @Test
    fun isParcelable() {
        val subject = LabeledSwatch(
                label = "My swatch",
                swatch = Palette.Swatch(0x3F51B5, 24)
        )

        val parceled = parcelReadWrite(subject, LabeledSwatch.CREATOR)

        assertEquals(subject.label, parceled.label)
        assertEquals(subject.swatch, parceled.swatch)
    }

    @Test
    fun isParcelableWithNullSwatch() {
        val subject = LabeledSwatch(
                label = "My swatch",
                swatch = null
        )

        val parceled = parcelReadWrite(subject)

        assertEquals(subject.label, parceled.label, LabeledSwatch.CREATOR)
        assertNull(parceled.swatch)
    }

    @Test
    fun canWriteAsParcelable() {
        val subject = LabeledSwatch(
                label = "My swatch",
                swatch = Palette.Swatch(0x3F51B5, 24)
        )

        val parceled = parcelReadWrite(subject)

        assertEquals(subject.label, parceled.label)
        assertEquals(subject.swatch, parceled.swatch)
    }
}