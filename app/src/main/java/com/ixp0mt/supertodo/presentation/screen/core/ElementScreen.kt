package com.ixp0mt.supertodo.presentation.screen.core

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_DeleteAlertDialog
import com.ixp0mt.supertodo.presentation.component.ST_ElementInfo
import com.ixp0mt.supertodo.presentation.component.ST_ListElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun ElementScreen(
    viewModel: ElementViewModel,
    screenState: ScreenState,
    onElementClick: (ElementParam) -> Unit,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreen() }
    }

    val backClick by viewModel.backClick.observeAsState()
    val editClick by viewModel.editClick.observeAsState()
    val internalElementClickInfo by viewModel.internalElementClickInfo.observeAsState()
    val showDialogDelete by viewModel.showDialogDelete.observeAsState()

    val elementInfo by viewModel.elementInfo.observeAsState()
    val pedigree by viewModel.pedigree.observeAsState()

    val listInternalFolders by viewModel.listFolders.observeAsState()
    val listInternalProjects by viewModel.listProjects.observeAsState()
    val listInternalTasks by viewModel.listTasks.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(editClick) { editClick?.let { onEditClick() } }
    LaunchedEffect(internalElementClickInfo) {
        internalElementClickInfo?.let {
            onElementClick(it)
        }
    }


    Column {
        ST_ElementInfo(
            name = elementInfo?.name ?: "",
            description = elementInfo?.description ?: "",
            strPedigree = pedigree!!
        )
        ST_ListElement(
            listFolders = listInternalFolders!!,
            listProjects = listInternalProjects!!,
            listTasks = listInternalTasks!!,
            onSpecialClick = { viewModel.markCompleteElement(it) },
            onElementClick = { typeElement, idElement ->
                viewModel.elementClick(typeElement, idElement)
            }
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