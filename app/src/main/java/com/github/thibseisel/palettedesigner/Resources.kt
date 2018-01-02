package com.github.thibseisel.palettedesigner

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Inflate a new view hierarchy from XML as this view children.
 *
 * @receiver The view that will be the parent for the inflated view hierarchy.
 * @param resource ID for an XML layout resource to load.
 * @param attachToRoot Whether the inflated view hierarchy should be attached to its parent.
 * If `false`, root is only used to create the correct LayoutParams subclass.
 * @return The root View of the inflated hierarchy. If `attachToRoot` is `true`, it is `this`,
 * otherwise it is the root of the inflated XML file.
 */
fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(this.context).inflate(resource, this, attachToRoot)