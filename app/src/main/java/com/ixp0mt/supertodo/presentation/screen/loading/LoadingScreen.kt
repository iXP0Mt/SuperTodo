package com.ixp0mt.supertodo.presentation.screen.loading

import androidx.compose.runtime.Composable

@Composable
fun LoadingScreen(
    onComplete: () -> Unit
) {
    onComplete()
}