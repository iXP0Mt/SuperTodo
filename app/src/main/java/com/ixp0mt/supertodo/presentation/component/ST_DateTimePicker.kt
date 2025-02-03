package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.ixp0mt.supertodo.presentation.util.DateTimePickerInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ST_DateTimePicker(
    label: String,
    dateTimePickerInfo: DateTimePickerInfo,
    showSelectTime: Boolean,
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    onDatePicker: (DatePickerState) -> Unit,
    onTimePicker: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    Row {
        Text(text = label)

        Button(
            onClick = { onDatePicker(datePickerState) }
        ) {
            Text(text = dateTimePickerInfo.labelDate)
        }

        if(showSelectTime) {
            Button(
                onClick = { onTimePicker() }
            ) {
                Text(text = dateTimePickerInfo.labelTime)
            }
        }
    }

    if(showDatePicker) {
        ST_DatePickerDialog(
            datePickerState = datePickerState,
            onDateSelected = onDateSelected,
            onDismiss = onDismiss
        )
    }

    if(showTimePicker) {
        ST_TimePickerDialog(
            initialMinute = dateTimePickerInfo.minute,
            initialHour = dateTimePickerInfo.hour,
            onDismiss = onDismiss,
            onTimeSelected = { hour, minute ->
                onTimeSelected(hour, minute)
            },
        )
    }
}