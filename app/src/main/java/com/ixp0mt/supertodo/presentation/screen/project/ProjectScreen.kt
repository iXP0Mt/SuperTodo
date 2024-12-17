package com.ixp0mt.supertodo.presentation.screen.project

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.BaseScreen
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.EssentialElementScreen

@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onElementClick: (ElementParam) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = onBackClick
    )

    EssentialElementScreen(
        viewModel = viewModel,
        onEditClick = onEditClick,
        onElementClick = onElementClick
    )
}