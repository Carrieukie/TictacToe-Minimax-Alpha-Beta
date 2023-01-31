package com.example.minimaxtictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.example.minimaxtictactoe.persistence.UserPreferencesRepository
import com.example.minimaxtictactoe.persistence.dataStore
import com.example.minimaxtictactoe.state.GameState
import com.example.minimaxtictactoe.ui.components.MainScreen
import com.example.minimaxtictactoe.ui.theme.Theme
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferencesRepository = UserPreferencesRepository(dataStore, this)
        val viewModel = TicTacToeViewModel(userPreferencesRepository)

        setContent {
            val gameState = viewModel.ticTacToeState.collectAsState(GameState()).value
            val theme = viewModel.themeStream.collectAsState(
                initial = Theme.FOLLOW_SYSTEM.themeValue,
                context = Dispatchers.Main.immediate
            ).value
            MainScreen(
                theme = theme,
                gameState = gameState,
                updateBoard = viewModel::updateField,
                resetGame = viewModel::resetGame,
                setTheme = viewModel::setTheme
            )
        }
    }

}

