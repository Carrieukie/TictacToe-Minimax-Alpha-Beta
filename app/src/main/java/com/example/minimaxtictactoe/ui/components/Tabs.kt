package com.example.minimaxtictactoe.ui.components

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabIndicatorScope
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.R
import com.example.minimaxtictactoe.ui.mainscreencontent.model.GameState
import com.example.minimaxtictactoe.ui.mainscreencontent.model.TabItem
import kotlinx.coroutines.launch

@Composable
fun Tabs(
    gameState: State<GameState>,
) {
    var state by remember { mutableIntStateOf(0) }
    LaunchedEffect(gameState.value.currentPlayerIndex) {
        state = gameState.value.currentPlayerIndex
        Log.i("Current player index", "${gameState.value.currentPlayerIndex}")
    }

    val tabs = listOf(
        TabItem.You(icon = R.drawable.ic_tic_tac_toe_x, playerName = "You", score = 0),
        TabItem.Draw(icon = R.drawable.ic_tic_tac_toe_tie, playerName = "Draw", score = 0),
        TabItem.AI(icon = R.drawable.ic_tic_tac_toe_o, playerName = "AI", score = 0)
    )

    Column {
        TabRow(
            selectedTabIndex = state,
            divider = {}
        ) {
            tabs.forEachIndexed { index, tabItem ->
                Tab(
                    modifier = Modifier
                        .wrapContentSize(),
                    selected = state == index,
                    onClick = {},
                    text = {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    text = tabItem.playerName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Image(
                                    painter = painterResource(
                                        id = tabItem.icon
                                    ),
                                    colorFilter = ColorFilter.tint(
                                        if (tabItem.playerName == "You") MaterialTheme.colorScheme.primary else if (tabItem.playerName == "AI") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                )
                                AnimatedCounter(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    count = when (index) {
                                        0 -> gameState.value.humanWins
                                        1 -> gameState.value.gamesTied
                                        else -> gameState.value.aiWins
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }
                )
            }
        }

    }
}


