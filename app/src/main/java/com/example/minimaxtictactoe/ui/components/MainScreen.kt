package com.example.minimaxtictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
    tabs: List<TabItem>,
    updateBoard: (row: Int, col: Int, player: Player) -> Unit,
    resetGame: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)
    val coroutineScope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(items[0]) }
    val pagerState = rememberPagerState(gameState.currentPlayerIndex)
    val density = LocalDensity.current

    MiniMaxTicTacToeTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent(
                        items = items,
                        selectedItem = selectedItem,
                        coroutineScope = coroutineScope,
                        drawerState = drawerState
                    )
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Round ${gameState.round}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

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

                        Row(
                            modifier = Modifier
                                .height(40.dp)
                        ) {
                            AnimatedVisibility(
                                visible = gameState.winner != null,
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
                                    resetGame()
                                }) {
                                    Text(text = "Retry")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}



