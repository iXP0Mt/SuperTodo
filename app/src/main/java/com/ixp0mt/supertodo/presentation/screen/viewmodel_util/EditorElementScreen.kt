package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_LocationCard
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.delay

@Composable
fun EditorElementScreen(
    viewModel: EditorElementViewModel,
    screenState: ScreenState,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onClickChangeLocation: (ElementParam) -> Unit,
    onSuccessSave: (ElementParam) -> Unit,
    specialField: (@Composable () -> Unit)? = null
) {
    BaseScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = onBackClick
    )

    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val errorMsg by viewModel.errorMsg.observeAsState()
    val showKeyboard by viewModel.showKeyboard.observeAsState()
    val textElement by viewModel.textElement.observeAsState()
    val descElement by viewModel.descElement.observeAsState()
    val iconIdLocation by viewModel.iconIdLocation.observeAsState()
    val locationName by viewModel.locationName.observeAsState()
    val infoClickChangeLocation by viewModel.infoClickChangeLocation.observeAsState()

    val paramSavedElement by viewModel.paramSavedElement.observeAsState()

    LaunchedEffect(paramSavedElement) { paramSavedElement?.let(onSuccessSave) }
    LaunchedEffect(infoClickChangeLocation) { infoClickChangeLocation?.let(onClickChangeLocation) }
    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            showSnackbar(snackbarHostState, scope, it)
            viewModel.clearErrorMsg()
        }
    }

    LaunchedEffect(showKeyboard) {
        showKeyboard?.let { show ->
            if(show) {
                focusRequester.requestFocus()
                delay(100)
                keyboard?.show()
            } else {
                keyboard?.hide()
            }
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
                placeholderText = "Текст",
                focusRequester = focusRequester
            )

            Spacer(modifier = Modifier.height(10.dp))

            ST_TextField(
                value = descElement!!,
                onValueChange = { viewModel.changeDescElement(it) },
                placeholderText = "Описание"
            )

            Spacer(modifier = Modifier.height(10.dp))

            specialField?.let {
                it()
                Spacer(modifier = Modifier.height(10.dp))
            }

            ST_LocationCard(
                name = locationName!!,
                idIcon = iconIdLocation,
                onClick = { viewModel.onLocationClick() }
            )
        }
    }
}