package com.ixp0mt.supertodo.presentation.screen.project.create

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementScreen

@Composable
fun CreateProjectScreen(
    viewModel: CreateProjectViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onSuccessSave: (ElementParam) -> Unit,
    onBackClick: () -> Unit
) {
    CreateElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        onSuccessSave = onSuccessSave,
        onBackClick = onBackClick,
        placeholder1 = "Название проекта",
        placeholder2 = "Описание проекта"
    )
}