package com.ixp0mt.supertodo.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ixp0mt.supertodo.domain.util.TypeElement
import com.ixp0mt.supertodo.presentation.util.ElementCardInfo

@Composable
fun ST_ListElements(
    listElements: List<ElementCardInfo>,
    onSpecialClick: (TypeElement, Long, Boolean) -> Unit,
    onElementClick: (TypeElement, Long) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        items(listElements) {
            ST_ElementCard(
                item = it,
                onSpecialClick = { onSpecialClick(it.typeElement, it.id, it.isComplete) },
                onElementClick = { onElementClick(it.typeElement, it.id) }
            )
        }
    }
}