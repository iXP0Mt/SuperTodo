package com.ixp0mt.supertodo.presentation.screen.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.component.ST_ListElement
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun MainFolderScreen(
    viewModel: MainFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    onElementClick: (typeElement: TypeElement, idElement: Long) -> Unit
) {

    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val elementClickInfo by viewModel.elementClickInfo.observeAsState()

    val listInternalFolders by viewModel.listInternalFolders.observeAsState()
    val listInternalProjects by viewModel.listInternalProjects.observeAsState()
    val listInternalTasks by viewModel.listInternalTasks.observeAsState()

    LaunchedEffect(elementClickInfo) {
        elementClickInfo?.let {
            onElementClick(it.typeElement, it.idElement)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ST_ListElement(
            listFolders = listInternalFolders!!,
            listProjects = listInternalProjects!!,
            listTasks = listInternalTasks!!,
            onClickMain = {
                viewModel.elementMainClick(it)
            },
            onClickExtend = { typeElement, idElement ->
                viewModel.elementClick(typeElement, idElement)
            }
        )
    }
}