package com.example.minimaxtictactoe.ui.mainscreencontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.R
import com.example.minimaxtictactoe.ui.components.AnimatedCounter
import com.example.minimaxtictactoe.ui.components.Tabs
import com.example.minimaxtictactoe.ui.components.TicTacToeBoard
import com.example.minimaxtictactoe.ui.mainscreencontent.model.GameState
import com.example.minimaxtictactoe.ui.mainscreencontent.model.Player
import com.example.minimaxtictactoe.ui.mainscreencontent.model.TicTacToeEvent

@Composable
fun MainScreenContent(
    gameState: State<GameState>,
    onEvent: (TicTacToeEvent) -> Unit,
) {

    val density = LocalDensity.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Text(
                            text = "Round ",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        AnimatedCounter(
                            count = gameState.value.round,
                            style = MaterialTheme.typography.titleLarge,
                        )

                    }

                    if (gameState.value.message != null) {
                        Text(
                            modifier = Modifier
                                .padding(4.dp),
                            text = gameState.value.message ?: "",
                        )
                    }
                }

                Tabs(
                    gameState = gameState,
                )

                TicTacToeBoard(
                    board = gameState.value.field,
                    onClick = { row, col ->
                        onEvent(TicTacToeEvent.UpdateField(col, row, Player.Human))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f)
                )

                Row(
                    modifier = Modifier
                        .height(40.dp)
                ) {
                    AnimatedVisibility(
                        visible = gameState.value.winner != null,
                        enter = slideInVertically {
                            with(density) { -40.dp.roundToPx() }
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {

                        Button(onClick = {
                            onEvent(TicTacToeEvent.ResetGame)
                        }) {
                            Text(text = "Retry")
                        }
                    }
                }
            }

            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .graphicsLayer {
                        rotationY = 180f
                    }
                    .clickable {
                        onEvent(TicTacToeEvent.FlipDrawerState)
                    },
                painter = painterResource(id = R.drawable.menu),
                contentDescription = null
            )
        }
    }
}



