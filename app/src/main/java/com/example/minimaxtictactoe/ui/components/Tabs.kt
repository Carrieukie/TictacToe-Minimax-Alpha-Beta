package com.example.minimaxtictactoe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.minimaxtictactoe.models.TabItem
import com.example.minimaxtictactoe.state.GameState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    gameState: GameState,
    tabs: List<TabItem>,
    pagerState: PagerState,
    onClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(currentTabPosition = tabPositions[pagerState.currentPage])
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                modifier = Modifier
                    .wrapContentSize(),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        onClick(index)
                        pagerState.animateScrollToPage(index)
                    }
                },
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
                                style = MaterialTheme.typography.titleSmall
                            )
                            Image(
                                painter = painterResource(
                                    id = tabItem.icon
                                ),
                                colorFilter = ColorFilter.tint(
                                    if (tabItem.playerName == "You") MaterialTheme.colorScheme.primary else if(tabItem.playerName == "AI") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                                ),
                                contentDescription = null,
                                modifier = Modifier
                            )
                            Text(
                                modifier = Modifier
                                    .padding(12.dp),
                                text = "${if (index == 0) gameState.humanWins else if (index == 1) gameState.gamesTied else gameState.aiWins}",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            )
        }
    }
}
