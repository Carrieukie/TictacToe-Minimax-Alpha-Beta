package com.example.minimaxtictactoe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.R
import com.example.minimaxtictactoe.models.Player
import com.example.minimaxtictactoe.models.TabItem
import com.example.minimaxtictactoe.state.GameState
import com.example.minimaxtictactoe.ui.theme.MiniMaxTicTacToeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    gameState: GameState,
    updateBoard: (row: Int, col: Int, player: Player) -> Unit
) {
    MiniMaxTicTacToeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val tabs = listOf(
                TabItem.You(icon = R.drawable.ic_tic_tac_toe_x, playerName = "You", score = 0),
                TabItem.Draw(icon = R.drawable.ic_tic_tac_toe_tie, playerName = "Draw", score = 0),
                TabItem.AI(icon = R.drawable.ic_tic_tac_toe_o, playerName = "AI", score = 0)
            )

            val pagerState = rememberPagerState(gameState.currentPlayerIndex)

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "Round 1")

                Tabs(
                    gameState,
                    tabs = tabs,
                    pagerState = pagerState,
                    onClick = {

                    })

                TicTacToeBoard(
                    board = gameState.field,
                    onClick = { row, col ->
                        updateBoard(col, row, Player.Human)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f)
                )

                Button(onClick = {}) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

