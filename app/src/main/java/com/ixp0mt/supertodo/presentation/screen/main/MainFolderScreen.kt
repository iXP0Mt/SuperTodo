package com.ixp0mt.supertodo.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_ListElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun MainFolderScreen(
    viewModel: MainFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    onElementClick: (ElementParam) -> Unit
) {

    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreen() }
    }

    val internalElementClickInfo by viewModel.internalElementClickInfo.observeAsState()

    val listInternalFolders by viewModel.listFolders.observeAsState()
    val listInternalProjects by viewModel.listProjects.observeAsState()
    val listInternalTasks by viewModel.listTasks.observeAsState()

    LaunchedEffect(internalElementClickInfo) {
        internalElementClickInfo?.let {
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
            onSpecialClick = { viewModel.markCompleteElement(it) },
            onElementClick = { typeElement, idElement ->
                viewModel.elementClick(typeElement, idElement)
            }
        )
    }
}