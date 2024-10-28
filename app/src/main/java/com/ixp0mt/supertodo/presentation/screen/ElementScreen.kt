package com.ixp0mt.supertodo.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_DeleteAlertDialog
import com.ixp0mt.supertodo.presentation.component.ST_ListElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun ElementScreen(
    viewModel: ElementViewModel,
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onElementClick: (ElementParam) -> Unit,
    onDeleteElement: () -> Unit,
) {
    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val backClick by viewModel.backClick.observeAsState()
    val editClick by viewModel.editClick.observeAsState()
    val elementClickInfo by viewModel.elementClickInfo.observeAsState()
    val showDialogDelete by viewModel.showDialogDelete.observeAsState(false)

    val listInternalFolders by viewModel.listInternalFolders.observeAsState()
    val listInternalProjects by viewModel.listInternalProjects.observeAsState()
    val listInternalTasks by viewModel.listInternalTasks.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(editClick) { if (editClick!!) onEditClick() }
    LaunchedEffect(elementClickInfo) {
        elementClickInfo?.let {
            onElementClick(it)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ST_ListElement(
            listFolders = listInternalFolders!!,
            listProjects = listInternalProjects!!,
            listTasks = listInternalTasks!!,
            onClickMain = { viewModel.elementMainClick(it) },
            onClickExtend = { typeElement, idElement ->
                viewModel.elementClick(typeElement, idElement)
            }
        )
    }

    if (showDialogDelete) {
        ST_DeleteAlertDialog(
            onDismissRequest = {
                viewModel.cancelDialogDelete()
            },
            onConfirmClick = {
                onDeleteElement()
                viewModel.cancelDialogDelete()
            }
        )
    }
}