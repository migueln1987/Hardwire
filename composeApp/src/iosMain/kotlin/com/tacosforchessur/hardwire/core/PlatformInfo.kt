package com.tacosforchessur.hardwire.core

import platform.UIKit.UIDevice

actual fun getDeviceInfo(): String {
    val device = UIDevice.currentDevice
    return "iOS ${device.systemVersion} - ${device.model}"
}