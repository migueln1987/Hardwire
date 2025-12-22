package com.tacosforchessur.hardwire.core

import android.os.Build

actual fun getDeviceInfo(): String {
    return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT}) - ${Build.MODEL}"
}