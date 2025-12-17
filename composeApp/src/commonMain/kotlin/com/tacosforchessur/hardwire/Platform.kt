package com.tacosforchessur.hardwire

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform