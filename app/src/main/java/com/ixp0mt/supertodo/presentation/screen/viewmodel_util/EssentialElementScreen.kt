package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_DeleteAlertDialog
import com.ixp0mt.supertodo.presentation.component.ST_ElementInfo

@Composable
fun EssentialElementScreen(
    viewModel: EssentialElementViewModel,
    onEditClick: () -> Unit,
    onElementClick: (ElementParam) -> Unit,
    specialField: (@Composable () -> Unit)? = null
) {
    val editClick by viewModel.editClick.observeAsState()
    val showDialogDelete by viewModel.showDialogDelete.observeAsState()
    val pedigree by viewModel.pedigree.observeAsState()
    val currentElement by viewModel.currentElement.observeAsState()

    LaunchedEffect(editClick) { editClick?.let { onEditClick() } }


    Column {
        ST_ElementInfo(
            name = currentElement?.name ?: "",
            description = currentElement?.description ?: "",
            strPedigree = pedigree!!,
            specialField = specialField
        )

        ElementScreen(
            viewModel = viewModel,
            onElementClick = onElementClick
        )
    }

    if (showDialogDelete!!) {
        ST_DeleteAlertDialog(
            onDismissRequest = {
                viewModel.cancelDialogDelete()
            },
            onConfirmClick = {
                viewModel.deleteCurrentElement()
            }
        )
    }
}