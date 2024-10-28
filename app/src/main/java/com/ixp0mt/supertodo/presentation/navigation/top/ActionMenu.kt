package com.ixp0mt.supertodo.presentation.navigation.top

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

@Composable
fun ActionMenu(
    items: List<ActionTopBar>
) {
    items.forEach { item ->
        key(item.icon) {
            IconButton(onClick = {
                item.onClick()
            }) {
                Icon(imageVector = item.icon, contentDescription = item.contentDescription)
            }
        }
    }
}