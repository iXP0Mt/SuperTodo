package com.ixp0mt.supertodo.presentation.screen.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.BaseScreen
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.ElementScreen

@Composable
fun MainFolderScreen(
    viewModel: MainFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    onElementClick: (ElementParam) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = {}
    )

    ElementScreen(
        viewModel = viewModel,
        onElementClick = onElementClick
    )
}