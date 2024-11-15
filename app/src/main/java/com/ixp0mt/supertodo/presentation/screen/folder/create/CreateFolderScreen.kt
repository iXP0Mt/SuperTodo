package com.ixp0mt.supertodo.presentation.screen.folder.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import kotlinx.coroutines.delay

@Composable
fun CreateFolderScreen(
    viewModel: CreateFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onSuccessSave: (idFolder: Long) -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val backClick by viewModel.backClick.observeAsState()
    val idNewFolder by viewModel.idNewFolder.observeAsState()
    val errorMsg by viewModel.errorMsg.observeAsState()
    val showKeyboard by viewModel.showKeyboard.observeAsState()

    val nameFolder by viewModel.nameFolder.observeAsState()
    val description by viewModel.description.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(idNewFolder) { idNewFolder?.let(onSuccessSave) }
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
                value = nameFolder!!,
                onValueChange = { viewModel.changeNameFolder(it) },
                placeholderText = "Название папки",
                focusRequester = focusRequester
            )

            ST_TextField(
                value = description!!,
                onValueChange = { viewModel.changeDescription(it) },
                placeholderText = "Описание папки",
            )

            /*Button(
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_task),
                    contentDescription = null
                )
                Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = item.name,
                    style = if(item.dateCompleted == null) {
                        LocalTextStyle.current
                    } else {
                        LocalTextStyle.current.copy(
                            textDecoration = TextDecoration.LineThrough,
                        )
                    }
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 10.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }*/
        }
    }
}