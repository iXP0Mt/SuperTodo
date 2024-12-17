package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun BaseScreen(
    viewModel: BaseViewModel,
    screenState: ScreenState,
    onBackClick: () -> Unit
) {
    DisposableEffect(Unit) {
        viewModel.initBase(screenState)
        onDispose { viewModel.clearBase() }
    }

    val backClick by viewModel.backClick.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
}