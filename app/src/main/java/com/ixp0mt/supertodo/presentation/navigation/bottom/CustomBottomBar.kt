package com.ixp0mt.supertodo.presentation.navigation.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomBar(
    viewModel: CustomBottomBarViewModel,
    currentRoute: String,
    onNavigationClick: (route: String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.initData()
    }

    LaunchedEffect(currentRoute) {
        viewModel.resetBottomBar()
    }

    val bottomBarItems by viewModel.bottomBarItems.observeAsState()
    val navigationRoute by viewModel.navigationRoute.observeAsState()
    val centerButtonIcon by viewModel.centerButtonIcon.observeAsState()

    LaunchedEffect(navigationRoute) {
        navigationRoute?.let {
            onNavigationClick(it)
            viewModel.resetNavigation()
        }
    }


    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            modifier = Modifier
                .height(75.dp)
                .offset(y = 35.dp),
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            bottomBarItems!!.forEach { item ->
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        viewModel.onItemClick(item)
                    },
                    icon = {
                        item.iconId?.let { iconId ->
                            Icon(
                                modifier = Modifier.size(32.dp),
                                imageVector = ImageVector.vectorResource(id = iconId),
                                contentDescription = null
                            )
                        }
                    },
                    enabled = item.isItemEnabled(currentRoute),
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-15).dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                FloatingActionButton(
                    onClick = { viewModel.onCenterButtonClick() },
                    modifier = Modifier,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {

                    centerButtonIcon?.let {
                        Icon(
                            modifier = Modifier.size(90.dp),
                            imageVector = ImageVector.vectorResource(id = it),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }

                }
            }
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
//    override fun defaultColor() = Color.Unspecified
    override fun defaultColor() = MaterialTheme.colorScheme.background

    @Composable
    override fun rippleAlpha() = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}