package com.ixp0mt.supertodo.presentation.screen.folder.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun EditFolderScreen(
    viewModel: EditFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val backClick by viewModel.backClick.observeAsState()
    val errorMsg by viewModel.errorMsg.observeAsState()

    val keyboard = LocalSoftwareKeyboardController.current
    val showKeyboard by viewModel.showKeyboard.observeAsState()

    val nameFolder by viewModel.nameFolder.observeAsState()
    val description by viewModel.description.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            showSnackbar(snackbarHostState, scope, it)
            viewModel.clearErrorMsg()
        }
    }
    LaunchedEffect(showKeyboard) { if(showKeyboard == false) keyboard?.hide() }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ST_TextField(
                value = nameFolder!!,
                onValueChange = { viewModel.changeNameFolder(it) },
                placeholderText = "Название папки"
            )

            ST_TextField(
                value = description!!,
                onValueChange = { viewModel.changeDescription(it) },
                placeholderText = "Описание папки"
            )
        }
    }
}