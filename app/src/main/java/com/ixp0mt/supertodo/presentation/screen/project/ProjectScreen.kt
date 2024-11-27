package com.ixp0mt.supertodo.presentation.screen.project

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.screen.core.ElementScreen

@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel = hiltViewModel(),
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

    /*ElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = { onBackClick() },
        onEditClick = { onEditClick() },
        onElementClick = { onElementClick(it.typeElement, it.idElement) },
    )*/
}