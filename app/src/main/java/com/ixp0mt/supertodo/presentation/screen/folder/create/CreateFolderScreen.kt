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
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_TextField
import com.ixp0mt.supertodo.presentation.component.showSnackbar
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.core.CreateElementScreen
import kotlinx.coroutines.delay

@Composable
fun CreateFolderScreen(
    viewModel: CreateFolderViewModel = hiltViewModel(),
    screenState: ScreenState,
    snackbarHostState: SnackbarHostState,
    onSuccessSave: (ElementParam) -> Unit,
    onBackClick: () -> Unit
) {
    CreateElementScreen(
        viewModel = viewModel,
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        onSuccessSave = onSuccessSave,
        onBackClick = onBackClick,
        placeholder1 = "Название папки",
        placeholder2 = "Описание папки"
    )
}