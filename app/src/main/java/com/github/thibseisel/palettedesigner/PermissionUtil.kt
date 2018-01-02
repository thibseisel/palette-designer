package com.github.thibseisel.palettedesigner

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class PermissionDenialException(val requiredPermission: String) : Exception() {
    override val message: String = "Required permission: [$requiredPermission]"
}

/**
 * Assert that the application has the specified permission granted,
 * or else throw a [PermissionDenialException].
 *
 * @receiver The context of any component from this application.
 * @param permission The constant describing the permission from [android.Manifest].
 * @throws PermissionDenialException if permission to the requested feature has not been granted.
 */
fun Context.checkPermissionIsGranted(permission: String) {
    if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
        throw PermissionDenialException(permission)
    }
}

/**
 * Determine if this application has the specified permission granted.
 *
 * @receiver The context of any component from this application.
 * @param permission The constant describing the permission from [android.Manifest].
 * @return `true` if permission has been granted.
 */
fun Context.hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


