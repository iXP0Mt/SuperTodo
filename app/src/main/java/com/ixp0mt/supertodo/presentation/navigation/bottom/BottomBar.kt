package com.ixp0mt.supertodo.presentation.navigation.bottom

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    viewModel: BottomBarViewModel,
    currentRoute: String?,
    onNavigationClick: (route: String) -> Unit
) {

    val bottomBarContain by viewModel.bottomBarContain.observeAsState()
    val navigationRoute by viewModel.navigationRoute.observeAsState()
    val enableBackHandler by viewModel.enableBackHandler.observeAsState()

    LaunchedEffect(navigationRoute) {
        navigationRoute?.let {
            onNavigationClick(it)
            viewModel.resetNavigation()
        }
    }

    BackHandler(enableBackHandler!!) { viewModel.showMainPanel() }


    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            modifier = Modifier.height(120.dp),
            containerColor = Color.Transparent,
        ) {
            bottomBarContain!!.forEach { item ->
                NavigationBarItem(
                    selected = (item as? ActionBottomBar.Navigation)?.route == currentRoute,
                    onClick = {
                        viewModel.onButtonClicked(item)
                    },
                    icon = { item.iconId?.let { idIcon ->
                        Column(
                            verticalArrangement = Arrangement.Top
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(if(item is ActionBottomBar.Navigation) 32.dp else 90.dp),
                                imageVector = ImageVector.vectorResource(id = idIcon),
                                contentDescription = null,
                                tint = if(item is ActionBottomBar.Navigation) LocalContentColor.current else Color.Unspecified
                            )
                        }
                    } },
                    enabled = item.isItemEnabled(currentRoute),
                    label = item.label?.let { { Text(text = item.label!!) } },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                )
            }
        }
    }
}


private object NoRippleTheme : RippleTheme {
    @Composable
//    override fun defaultColor() = Color.Unspecified
    override fun defaultColor() = MaterialTheme.colorScheme.background

    @Composable
    override fun rippleAlpha() = RippleAlpha(0.0f,0.0f,0.0f,0.0f)
}