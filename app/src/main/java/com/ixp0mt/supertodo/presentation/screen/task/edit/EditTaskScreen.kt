package com.ixp0mt.supertodo.presentation.screen.task.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.EditElementScreen

@Composable
fun EditTaskScreen(
    viewModel: EditTaskViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onLocationClick: (LocationParam) -> Unit
) {
    EditElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onLocationClick = onLocationClick,
        placeholder1 = "Текст задачи",
        placeholder2 = "Описание задачи"
    )
}