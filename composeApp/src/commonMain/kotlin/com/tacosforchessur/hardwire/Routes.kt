package com.tacosforchessur.hardwire

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class MetronomeRoute(val initialBpm: Int = 120)