package com.ixp0mt.supertodo.presentation.screen.viewmodel_util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.ixp0mt.supertodo.domain.model.ElementParam
import com.ixp0mt.supertodo.presentation.component.ST_ListElements

@Composable
fun ElementScreen(
    viewModel: ElementViewModel,
    onElementClick: (ElementParam) -> Unit,
) {
    val infoClickSubElement by viewModel.infoClickSubElement.observeAsState()
    val listSubElements by viewModel.listSubElements.observeAsState()

    LaunchedEffect(infoClickSubElement) {
        infoClickSubElement?.let {
            onElementClick(it)
        }
    }


    ST_ListElements(
        listElements = listSubElements!!,
        onSpecialClick = { typeElement, id, isComplete ->
            viewModel.markElementComplete(typeElement, id, isComplete)
        },
        onElementClick = { typeElement, id ->
            viewModel.elementClick(typeElement, id)
        }
    )
}