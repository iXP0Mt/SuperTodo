package com.ixp0mt.supertodo.presentation.navigation.top

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.ixp0mt.supertodo.presentation.navigation.screen.ScreenState


@Composable
fun TopBar(
    screenState: ScreenState,
) {
    @OptIn(ExperimentalMaterial3Api::class)
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                color = MaterialTheme.colorScheme.onPrimary,
                text = screenState.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            val icon = screenState.navigationIcon
            val callback = screenState.onNavigationIconClick

            if(icon != null) {
                IconButton(onClick = {
                    callback?.invoke()
                }) {
                    Icon(imageVector = icon, contentDescription = screenState.navigationIconContentDescription)
                }
            }
        },
        actions = {
            val items = screenState.actionsTopBar
            if(items.isNotEmpty()) {
                ActionMenu(items)
            }
        }
    )
}