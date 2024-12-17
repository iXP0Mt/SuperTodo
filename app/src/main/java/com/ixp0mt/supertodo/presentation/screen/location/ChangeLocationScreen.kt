package com.ixp0mt.supertodo.presentation.screen.location

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_ElementCardSimple
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState
import com.ixp0mt.supertodo.presentation.screen.viewmodel_util.BaseScreen

@Composable
fun ChangeLocationScreen(
    viewModel: ChangeLocationViewModel = hiltViewModel(),
    screenState: ScreenState,
    onSaveClick: (ElementParam) -> Unit,
    onElementClick: (ElementParam) -> Unit,
    onBackClick: () -> Unit
) {
    val locationAsElement by viewModel.locationAsElement.observeAsState()
    val listSubElements by viewModel.listSubElements.observeAsState()
    val iconIdLocation by viewModel.iconIdLocation.observeAsState()

    val showBackLocation by viewModel.showBackLocation.observeAsState()
    val infoClickSave by viewModel.infoClickSave.observeAsState()
    val infoClickSubElement by viewModel.infoClickSubElement.observeAsState()

    LaunchedEffect(infoClickSave) { infoClickSave?.let { onSaveClick(it) } }
    LaunchedEffect(infoClickSubElement) {
        infoClickSubElement?.let {
            onElementClick(it)
        }
    }


    BaseScreen(
        viewModel = viewModel,
        screenState = screenState,
        onBackClick = onBackClick
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(MaterialTheme.colorScheme.primary.copy(0.25f)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconIdLocation?.let {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = null
                )
            }
            locationAsElement?.let {
                Text(text = it.name)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (showBackLocation!!) {
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

            items(listSubElements!!) {
                ST_ElementCardSimple(
                    item = it,
                    onClick = { viewModel.elementClick(it.typeElement, it.idElement) }
                )
            }
        }
    }
}