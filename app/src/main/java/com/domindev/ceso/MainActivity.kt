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
import com.domindev.ceso.presentation.ui.screens.AddNoteScreen
import com.domindev.ceso.presentation.ui.screens.HomeScreen
import com.domindev.ceso.presentation.ui.viewmodel.DataViewModel
import com.domindev.ceso.presentation.ui.theme.CesoTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<DataViewModel> (
                factory = ViewModelFactoryHelper(MyApp.appModule.dao)
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            CesoTheme {
                NavHost(
                    navController = navController,
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
                        HomeScreen(state, viewModel::onEvent) {
                            navController.navigate(it)
                        }
                    }
                    composable<AddNoteScreen> {
                        AddNoteScreen(state, viewModel::onEvent)
                    }
                }
            }
        }
    }
}

@Serializable
object HomeScreen

@Serializable
object AddNoteScreen