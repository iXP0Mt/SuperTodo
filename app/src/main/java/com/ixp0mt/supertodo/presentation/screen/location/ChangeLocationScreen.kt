package com.ixp0mt.supertodo.presentation.screen.location

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.R
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.domain.model.LocationParam
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.domain.util.TypeLocation
import com.ixp0mt.supertodo.presentation.component.ST_FolderCardSimple
import com.ixp0mt.supertodo.presentation.component.ST_ProjectCardSimple
import com.ixp0mt.supertodo.presentation.component.ST_TaskCardSimple
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState

@Composable
fun ChangeLocationScreen(
    viewModel: ChangeLocationViewModel = hiltViewModel(),
    screenState: ScreenState,
    onBackClick: () -> Unit,
    onSaveClick: (LocationParam) -> Unit,
    onElementClick: (LocationParam) -> Unit
) {
    DisposableEffect(Unit) {
        viewModel.initScreen(screenState)
        onDispose { viewModel.clearScreenState() }
    }

    val backClick by viewModel.backClick.observeAsState()
    val saveClickInfo by viewModel.saveClickInfo.observeAsState()
    val elementClickInfo by viewModel.elementClickInfo.observeAsState()

    val listInternalFolders by viewModel.listInternalFolders.observeAsState()
    val listInternalProjects by viewModel.listInternalProjects.observeAsState()
    val listInternalTasks by viewModel.listInternalTasks.observeAsState()

    val elementOfLocation by viewModel.elementOfLocation.observeAsState()
    val showBackLocation by viewModel.showBackLocation.observeAsState()

    BackHandler { viewModel.handleBack() }
    LaunchedEffect(backClick) { backClick?.let { onBackClick() } }
    LaunchedEffect(saveClickInfo) { saveClickInfo?.let { onSaveClick(it) } }
    LaunchedEffect(elementClickInfo) {
        elementClickInfo?.let {
            onElementClick(it)
        }
    }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            viewModel.getIconLocation()?.let {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = null
                )
            }
            Text(text = elementOfLocation!!.name)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if(showBackLocation!!) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .clickable { viewModel.backLocationClick() },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RectangleShape,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                style = MaterialTheme.typography.labelMedium,
                                text = "<...>"
                            )
                        }
                    }
                }
            }

            items(listInternalFolders!!) {
                ST_FolderCardSimple(
                    item = it,
                    onClick = {
                        viewModel.elementClick(TypeElement.FOLDER, it.idFolder)
                    }
                )
            }
            items(listInternalProjects!!) {
                ST_ProjectCardSimple(
                    item = it,
                    onClick = { viewModel.elementClick(TypeElement.PROJECT, it.idProject) }
                )
            }
            items(listInternalTasks!!) {
                ST_TaskCardSimple(
                    item = it,
                    onClick = { viewModel.elementClick(TypeElement.TASK, it.idTask) }
                )
            }
        }
    }
}