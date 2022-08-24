package com.aberon.flexbook.tool

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission

class Permissions(private val context: Context) {
    companion object {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    }

    fun checkPermissions(): Boolean = permissions.any { permission ->
        checkCallingOrSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}