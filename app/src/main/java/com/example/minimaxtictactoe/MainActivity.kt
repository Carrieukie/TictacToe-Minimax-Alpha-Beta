package com.example.minimaxtictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import com.example.minimaxtictactoe.persistence.UserPreferencesRepository
import com.example.minimaxtictactoe.persistence.dataStore
import com.example.minimaxtictactoe.ui.mainscreencontent.model.GameState
import com.example.minimaxtictactoe.ui.components.DrawerContent
import com.example.minimaxtictactoe.ui.mainscreencontent.MainScreenContent
import com.example.minimaxtictactoe.ui.mainscreencontent.MainScreenViewModel
import com.example.minimaxtictactoe.ui.theme.MiniMaxTicTacToeTheme
import com.example.minimaxtictactoe.ui.theme.Theme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferencesRepository = UserPreferencesRepository(dataStore, this)
        val viewModel = MainScreenViewModel(userPreferencesRepository)

        setContent {
            val gameState = viewModel.ticTacToeState.collectAsState(GameState())
            val theme = viewModel.themeStream.collectAsState(
                initial = Theme.FOLLOW_SYSTEM.themeValue,
                context = Dispatchers.Main.immediate
            ).value

            val drawerState = rememberDrawerState(DrawerValue.Closed)

            MiniMaxTicTacToeTheme(
                theme = theme
            ) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(
                            drawerState = drawerState,
                            setTheme = viewModel::setTheme
                        )
                    }
                ) {
                    MainScreenContent(
                        gameState = gameState,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }

}

