package com.ixp0mt.supertodo.presentation.screen.task.edit

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.component.ST_LocationCard
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun EditTaskScreen(
    viewModel: EditTaskViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onLocationClick: (typeLocation: TypeLocation, idLocation: Long) -> Unit
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

    val taskInfo by viewModel.taskInfo.observeAsState()
    val nameTask by viewModel.nameTask.observeAsState()
    val description by viewModel.description.observeAsState()
    val nameLocation by viewModel.nameLocation.observeAsState()
    val idIcon by viewModel.idIcon.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            showSnackbar(snackbarHostState, scope, it)
            viewModel.clearErrorMsg()
        }
    }
    LaunchedEffect(showKeyboard) { if (showKeyboard == false) keyboard?.hide() }


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
                value = nameTask!!,
                onValueChange = { viewModel.changeNameTask(it) },
                placeholderText = "Название папки"
            )

            Spacer(modifier = Modifier.height(10.dp))

            ST_TextField(
                value = description!!,
                onValueChange = { viewModel.changeDescription(it) },
                placeholderText = "Описание папки"
            )

            Spacer(modifier = Modifier.height(10.dp))

            ST_LocationCard(
                name = nameLocation!!,
                idIcon = idIcon,
                onClick = { onLocationClick(taskInfo!!.typeLocation, taskInfo!!.idLocation ?: 0L) }
            )
        }
    }
}