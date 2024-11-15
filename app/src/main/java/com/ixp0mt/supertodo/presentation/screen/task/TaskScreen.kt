package com.ixp0mt.supertodo.presentation.screen.task

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.screen.core.ElementScreen

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onElementClick: (typeElement: TypeElement, idElement: Long) -> Unit
) {

    ElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = { onBackClick() },
        onEditClick = { onEditClick() },
        onElementClick = { onElementClick(it.typeElement, it.idElement) },
        onDeleteElement = { viewModel.deleteCurrentElement() }
    )

    /*DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val backClick by viewModel.backClick.observeAsState()
    val editClick by viewModel.editClick.observeAsState()
    val elementClickInfo by viewModel.elementClickInfo.observeAsState()
    val showDialogDelete by viewModel.showDialogDelete.observeAsState(false)

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(editClick) { if (editClick!!) onEditClick() }
    LaunchedEffect(elementClickInfo) {
        elementClickInfo?.let {
            onElementClick(
                it.typeElement,
                it.idElement
            )
        }
    }*/

    //val listInternalTasks by viewModel.listInternalTasks.observeAsState()


    /*Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ST_ListElement(
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
                viewModel.deleteCurrentElement()
                viewModel.cancelDialogDelete()
            }
        )
    }*/
}