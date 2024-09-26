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
import com.domindev.ceso.ui.navigation.NoteScreen
import com.domindev.ceso.ui.navigation.HomeScreen
import com.domindev.ceso.ui.screens.NoteScreen
import com.domindev.ceso.ui.screens.HomeScreen
import com.domindev.ceso.ui.viewmodel.DataViewModel
import com.domindev.ceso.ui.theme.CesoTheme

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
                        HomeScreen(viewModel, state, viewModel::onEvent) {
                            navController.navigate(it)
                        }
                    }
                    composable<NoteScreen> {
                        NoteScreen(state = state, onEvent = viewModel::onEvent) {
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }
}