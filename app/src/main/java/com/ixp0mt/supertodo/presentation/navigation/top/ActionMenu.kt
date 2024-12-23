package com.ixp0mt.supertodo.presentation.navigation.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun ActionMenu(
    listShownItems: List<ActionTopBar>,
    listOverflowItems: List<ActionTopBar>,
    onToggleOverflow: () -> Unit,
    isOpenDropdownMenu: Boolean
) {
    listShownItems.forEach { item ->
        key(item.icon) {
            IconButton(onClick = {
                item.onClick()
            }) {
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
            }
        }
    }

    if(listOverflowItems.isNotEmpty()) {
        IconButton(
            onClick = onToggleOverflow
        ) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        DropdownMenu(
            expanded = isOpenDropdownMenu,
            onDismissRequest = onToggleOverflow
        ) {
            listOverflowItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.title) },
                    onClick = item.onClick
                )
            }
        }
    }
}