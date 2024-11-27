package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SnackBar(
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding()
                .align(Alignment.BottomCenter)
                .padding(start = 50.dp, end = 50.dp)
        )
    }
}

fun showSnackbar(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    text: String
) {
    scope.launch {
        snackbarHostState.showSnackbar(message = text)
    }
}