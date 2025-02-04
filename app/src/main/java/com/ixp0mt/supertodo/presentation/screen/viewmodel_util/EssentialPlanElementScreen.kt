package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_DateTimeField

@Composable
fun EssentialPlanElementScreen(
    viewModel: EssentialPlanElementViewModel,
    onEditClick: () -> Unit,
    onElementClick: (ElementParam) -> Unit
) {
    val labelStartDate by viewModel.labelStartDate.observeAsState()
    val labelStartTime by viewModel.labelStartTime.observeAsState()
    val labelEndDate by viewModel.labelEndDate.observeAsState()
    val labelEndTime by viewModel.labelEndTime.observeAsState()

    EssentialElementScreen(
        viewModel = viewModel,
        onEditClick = onEditClick,
        onElementClick = onElementClick,
        specialField = {
            ST_DateTimeField(
                labelStartDate = labelStartDate,
                labelStartTime = labelStartTime,
                labelEndDate = labelEndDate,
                labelEndTime = labelEndTime
            )
        }
    )
}

