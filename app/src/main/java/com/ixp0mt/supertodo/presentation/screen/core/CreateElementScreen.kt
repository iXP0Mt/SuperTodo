package com.ixp0mt.supertodo.presentation.screen.core

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.delay

@Composable
fun CreateElementScreen(
    viewModel: CreateElementViewModel,
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onSuccessSave: (ElementParam) -> Unit,
    onBackClick: () -> Unit,
    placeholder1: String,
    placeholder2: String
) {
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreen() }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val backClick by viewModel.backClick.observeAsState()
    val errorMsg by viewModel.errorMsg.observeAsState()
    val showKeyboard by viewModel.showKeyboard.observeAsState()

    val paramNewElement by viewModel.paramNewElement.observeAsState()

    val textElement by viewModel.textElement.observeAsState()
    val descElement by viewModel.descElement.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(paramNewElement) { paramNewElement?.let(onSuccessSave) }
    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            showSnackbar(snackbarHostState, scope, it)
            viewModel.clearErrorMsg()
        }
    }

    LaunchedEffect(showKeyboard) {
        if(showKeyboard!!) {
            focusRequester.requestFocus()
            delay(100)
            keyboard?.show()
        } else {
            keyboard?.hide()
        }
    }


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
                value = textElement!!,
                onValueChange = { viewModel.changeTextElement(it) },
                placeholderText = placeholder1,
                focusRequester = focusRequester
            )

            ST_TextField(
                value = descElement!!,
                onValueChange = { viewModel.changeDescElement(it) },
                placeholderText = placeholder2,
            )
        }
    }
}