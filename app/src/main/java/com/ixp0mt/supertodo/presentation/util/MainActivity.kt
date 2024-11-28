package com.ixp0mt.supertodo.presentation.util

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ixp0mt.supertodo.presentation.navigation.bottom.BottomBarViewModel
import com.ixp0mt.supertodo.presentation.navigation.screen.rememberScreenState
import com.ixp0mt.supertodo.presentation.screen.app_entry.AppEntryScreen
import com.ixp0mt.supertodo.presentation.screen.app_entry.AppEntryViewModel
import com.ixp0mt.supertodo.ui.theme.SupertodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appEntryViewModel: AppEntryViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by viewModels()

    //@SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.parseColor("#FFA7A7A7"),
                Color.parseColor("#FFA7A7A7")
            )
        )
        setContent {

            SupertodoTheme(dynamicColor = false) {
                val navController = rememberNavController()
                navController.enableOnBackPressed(true)

                val screenState = rememberScreenState(navController)
                val snackbarHostState = remember { SnackbarHostState() }

                AppEntryScreen(
                    screenState = screenState,
                    viewModel = appEntryViewModel,
                    bottomBarViewModel = bottomBarViewModel,
                    navHostController = navController,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}