package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ST_DateTimeField(
    labelStartDate: String?,
    labelStartTime: String?,
    labelEndDate: String?,
    labelEndTime: String?
) {
    Column {
        labelStartDate?.let {
            Text(
                text = "Начало: $labelStartDate в $labelStartTime"
            )
        }
        labelEndDate?.let {
            Text(
                text = "Конец: $labelEndDate в $labelEndTime"
            )
        }
    }
}