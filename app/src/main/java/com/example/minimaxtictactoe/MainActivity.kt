package com.example.minimaxtictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.minimaxtictactoe.models.TabItem
import com.example.minimaxtictactoe.state.GameState
import com.example.minimaxtictactoe.ui.components.MainScreen

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<TicTacToeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gameState = viewModel.ticTacToeState.collectAsState(GameState()).value
            val tabs = listOf(
                TabItem.You(icon = R.drawable.ic_tic_tac_toe_x, playerName = "You", score = 0),
                TabItem.Draw(icon = R.drawable.ic_tic_tac_toe_tie, playerName = "Draw", score = 0),
                TabItem.AI(icon = R.drawable.ic_tic_tac_toe_o, playerName = "AI", score = 0)
            )
            MainScreen(
                gameState = gameState,
                tabs = tabs,
                updateBoard = viewModel::updateField,
                resetGame = viewModel::resetGame
            )
        }
    }

}

