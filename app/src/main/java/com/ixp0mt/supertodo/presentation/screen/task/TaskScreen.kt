package com.ixp0mt.supertodo.presentation.screen.task

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementScreen

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onElementClick: (ElementParam) -> Unit
) {

    ElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onElementClick = onElementClick,
        onBackClick = onBackClick,
        onEditClick = onEditClick
    )
}