package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun EditorPlanElementScreen(
    viewModel: EditorPlanElementViewModel,
    screenState: ScreenState,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onClickChangeLocation: (ElementParam) -> Unit,
    onSuccessSave: (ElementParam) -> Unit,
) {
    EditorElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState,
        onClickChangeLocation = onClickChangeLocation,
        onSuccessSave = onSuccessSave,
        specialField = {
            DateTimePicker()
        }
    )
}

@Composable
fun DateTimePicker() {
    Text(text = "ПОЛЕ ДЛЯ ВЫБОРА ВРЕМЕНИ")
}

