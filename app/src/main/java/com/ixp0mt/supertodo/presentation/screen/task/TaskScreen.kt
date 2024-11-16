package com.ixp0mt.supertodo.presentation.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.component.ST_ElementInfo
import com.ixp0mt.supertodo.presentation.screen.core.ElementScreen

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onElementClick: (typeElement: TypeElement, idElement: Long) -> Unit
) {
    val taskInfo by viewModel.taskInfo.observeAsState()
    val listPedigree by viewModel.listPedigree.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ST_ElementInfo(
            name = taskInfo!!.name,
            description = taskInfo!!.description,
            listPedigree = listPedigree!!
        )

        ElementScreen(
            viewModel = viewModel,
            screenState = screenState,
            onBackClick = { onBackClick() },
            onEditClick = { onEditClick() },
            onElementClick = { onElementClick(it.typeElement, it.idElement) },
            onDeleteElement = { viewModel.deleteCurrentElement() }
        )
    }
}