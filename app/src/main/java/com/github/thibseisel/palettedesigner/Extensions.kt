package com.github.thibseisel.palettedesigner

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean = false) =
        LayoutInflater.from(this.context).inflate(layoutResId, this, attachToRoot)