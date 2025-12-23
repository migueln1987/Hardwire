package com.tacosforchessur.hardwire.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveScreen(
    modifier: Modifier = Modifier,
    tabletView: @Composable (() -> Unit)? = null,
    phoneView: @Composable (() -> Unit)? = null,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp
        if (isTablet) {
            tabletView?.invoke()
        } else {
            phoneView?.invoke()
        }
    }
}