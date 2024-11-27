package com.ixp0mt.supertodo.presentation.screen.folder

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.ElementScreen

@Composable
fun FolderScreen(
    viewModel: FolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    onElementClick: (ElementParam) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    ElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onElementClick = onElementClick,
        onBackClick = onBackClick,
        onEditClick = onEditClick
    )
}