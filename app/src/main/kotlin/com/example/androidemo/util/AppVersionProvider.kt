package com.example.androidemo.util

import android.content.Context
import android.content.pm.PackageManager

class AppVersionProvider(private val context: Context) {
    fun getCurrentVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                0
            )
            packageInfo.versionName ?: "1.0"
        } catch (e: Exception) {
            "1.0"
        }
    }
}
