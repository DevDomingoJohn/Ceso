package com.domindev.ceso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domindev.ceso.core.MyApp
import com.domindev.ceso.core.util.ViewModelFactoryHelper
import com.domindev.ceso.ui.screens.NoteScreen
import com.domindev.ceso.ui.screens.HomeScreen
import com.domindev.ceso.ui.screens.SettingsScreen
import com.domindev.ceso.ui.viewmodel.DataViewModel
import com.domindev.ceso.ui.theme.CesoTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<DataViewModel>(
                factory = ViewModelFactoryHelper(MyApp.appModule.dao)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            val themeState by MyApp.appModule.themeState.collectAsStateWithLifecycle()

            CesoTheme(darkTheme = themeState.isDarkTheme) {
                NavHost(
                    navController = navController,
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
                        HomeScreen(viewModel, state, navController, viewModel::onEvent) {
                            navController.navigate(it)
                        }
                    }
                    composable<NoteScreen> {
                        NoteScreen(state = state, onEvent = viewModel::onEvent) {
                            navController.navigateUp()
                        }
                    }
                    composable<SettingsScreen> {
                        SettingsScreen(state, navController, viewModel::onEvent) { route ->
                            if (route == null) navController.navigateUp()
                            else navController.navigate(route)
                        }
                    }
                }
            }
        }
    }
}

@Serializable
object HomeScreen

@Serializable
object NoteScreen

@Serializable
object SettingsScreen