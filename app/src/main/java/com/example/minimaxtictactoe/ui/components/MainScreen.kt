package com.example.minimaxtictactoe.ui.components

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
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.R
import com.example.minimaxtictactoe.models.Player
import com.example.minimaxtictactoe.models.TabItem
import com.example.minimaxtictactoe.models.ThemeWrapper
import com.example.minimaxtictactoe.state.GameState
import com.example.minimaxtictactoe.ui.theme.MiniMaxTicTacToeTheme
import com.example.minimaxtictactoe.ui.theme.Theme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    theme: Int,
    gameState: GameState,
    updateBoard: (row: Int, col: Int, player: Player) -> Unit,
    resetGame: () -> Unit,
    setTheme: (Int) -> Unit
) {
    val tabs = listOf(
        TabItem.You(icon = R.drawable.ic_tic_tac_toe_x, playerName = "You", score = 0),
        TabItem.Draw(icon = R.drawable.ic_tic_tac_toe_tie, playerName = "Draw", score = 0),
        TabItem.AI(icon = R.drawable.ic_tic_tac_toe_o, playerName = "AI", score = 0)
    )
    val items = listOf(
        ThemeWrapper.System("Use system theme", icon = Icons.Default.Settings, themeValu = Theme.FOLLOW_SYSTEM),
        ThemeWrapper.LightMode("Light Mode", icon = Icons.Default.ModeNight, themeValu = Theme.LIGHT_THEME),
        ThemeWrapper.DarkMode("Dark Mode", icon = Icons.Default.LightMode, themeValu = Theme.DARK_THEME),
        ThemeWrapper.MaterialYou("Material You", icon = Icons.Default.Mode, themeValu = Theme.MATERIAL_YOU),
        )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(items[0]) }
    val pagerState = rememberPagerState(gameState.currentPlayerIndex)
    val density = LocalDensity.current

    MiniMaxTicTacToeTheme(
        theme = theme
    ) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    items = items,
                    selectedItem = selectedItem,
                    coroutineScope = coroutineScope,
                    drawerState = drawerState,
                    setTheme = setTheme
                )
            }
        ) {
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
                                    count = gameState.round,
                                    style = MaterialTheme.typography.titleLarge,
                                )

                            }

                            if (gameState.message != null) {
                                Text(
                                    modifier = Modifier
                                        .padding(4.dp),
                                    text = gameState.message.toString()
                                )
                            }
                        }

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

                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .graphicsLayer {
                                rotationY = 180f

                            }
                            .clickable {
                                coroutineScope.launch {
                                    if (drawerState.isOpen) {
                                        drawerState.close()
                                    }else{
                                        drawerState.open()
                                    }
                                }
                            },
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = null
                    )
                }
            }
        }
    }
}



