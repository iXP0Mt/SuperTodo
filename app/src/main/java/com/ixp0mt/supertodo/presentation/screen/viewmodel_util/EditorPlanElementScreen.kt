package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_DateTimePicker
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.util.KindDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorPlanElementScreen(
    viewModel: EditorPlanElementViewModel,
    screenState: ScreenState,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onClickChangeLocation: (ElementParam) -> Unit,
    onSuccessSave: (ElementParam) -> Unit,
) {
    val startDateTimePickerInfo by viewModel.startDateTimePickerInfo.observeAsState()
    val endDateTimePickerInfo by viewModel.endDateTimePickerInfo.observeAsState()

    val showStartDateDialog by viewModel.showStartDateDialog.observeAsState()
    val showStartTimeDialog by viewModel.showStartTimeDialog.observeAsState()

    val showEndDateDialog by viewModel.showEndDateDialog.observeAsState()
    val showEndTimeDialog by viewModel.showEndTimeDialog.observeAsState()

    val showSelectStartTime by viewModel.showSelectStartTime.observeAsState()
    val showSelectEndTime by viewModel.showSelectEndTime.observeAsState()

    EditorElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState,
        onClickChangeLocation = onClickChangeLocation,
        onSuccessSave = onSuccessSave,
        specialField = {
            ST_DateTimePicker(
                label = "Начало",
                dateTimePickerInfo = startDateTimePickerInfo!!,
                showSelectTime = showSelectStartTime!!,
                showDatePicker = showStartDateDialog!!,
                showTimePicker = showStartTimeDialog!!,
                onDatePicker = { viewModel.onDatePicker(it, KindDateTime.START) },
                onTimePicker = { viewModel.onTimePicker(KindDateTime.START) },
                onDateSelected = { date -> viewModel.onDateSelected(date, KindDateTime.START) },
                onTimeSelected = { hour, minute -> viewModel.onTimeSelected(hour, minute, KindDateTime.START) },
                onDismiss = { viewModel.onDismissDialog() }
            )

            ST_DateTimePicker(
                label = "Конец",
                dateTimePickerInfo = endDateTimePickerInfo!!,
                showSelectTime = showSelectEndTime!!,
                showDatePicker = showEndDateDialog!!,
                showTimePicker = showEndTimeDialog!!,
                onDatePicker = { viewModel.onDatePicker(it, KindDateTime.END) },
                onTimePicker = { viewModel.onTimePicker(KindDateTime.END) },
                onDateSelected = { date -> viewModel.onDateSelected(date, KindDateTime.END) },
                onTimeSelected = { hour, minute -> viewModel.onTimeSelected(hour, minute, KindDateTime.END) },
                onDismiss = { viewModel.onDismissDialog() }
            )
        }
    )
}








