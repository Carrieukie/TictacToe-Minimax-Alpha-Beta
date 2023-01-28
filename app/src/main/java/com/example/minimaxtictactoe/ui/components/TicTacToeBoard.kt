package com.example.minimaxtictactoe.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.minimaxtictactoe.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToeBoard(
    board: Array<Array<Char?>>,
    modifier: Modifier = Modifier,
    onClick: (row: Int, col: Int) -> Unit
) {

    val dividerWidth = remember { 8.dp }

    BoxWithConstraints(modifier = modifier) {
        val tileSize = remember(maxWidth, dividerWidth) {
            (maxWidth / 3) - dividerWidth / 1.5f
        }

        for ((row, pointTypeRow) in board.withIndex()) {
            for ((col, pointChar) in pointTypeRow.withIndex()) {
                val endPadding = remember {
                    when {
                        col != 0 -> dividerWidth
                        else -> 0.dp
                    }
                }

                val bottomPadding = remember {
                    when {
                        row != 0 -> dividerWidth
                        else -> 0.dp
                    }
                }

                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .offset(
                            x = (tileSize * col) + (endPadding * col),
                            y = (tileSize * row) + (bottomPadding * row)
                        )
                        .size(tileSize)
                        .clickable {
                            if (pointChar == null) {
                                onClick(row, col)
                            }
                        }
                ) {
                    AnimatedVisibility(
                        visible = pointChar != null,
                        enter = scaleIn(
                            animationSpec = tween(400)
                        ),
                        exit = scaleOut(
                            animationSpec = tween(200)
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .matchParentSize()
                    ) {
                        PlayerImage(
                            playerChar = pointChar
                        )
                    }
                }
            }
        }

        for (i in 0 until 2) {
            val padding = remember {
                when {
                    i != 0 -> dividerWidth
                    else -> 0.dp
                }
            }

            BoardDivider(
                maximumHeight = maxHeight,
                lineWidth = { dividerWidth },
                verticalOffset = {
                    IntOffset(
                        x = (tileSize * (i + 1) + padding)
                            .toPx()
                            .toInt(),
                        y = 0.dp
                            .toPx()
                            .toInt()
                    )
                },
                horizontalOffset = {
                    IntOffset(
                        x = 0.dp
                            .toPx()
                            .toInt(),
                        y = (tileSize * (i + 1) + padding)
                            .toPx()
                            .toInt()
                    )
                }
            )
        }
    }
}

@Composable
private fun BoardDivider(
    maximumHeight: Dp,
    lineWidth: () -> Dp,
    horizontalOffset: Density.() -> IntOffset,
    verticalOffset: Density.() -> IntOffset
) {

    Divider(
        thickness = lineWidth(),
        modifier = Modifier
            .offset {
                verticalOffset()
            }
            .size(lineWidth(), maximumHeight)
            .clip(CircleShape)
    )

    Divider(
        thickness = lineWidth(),
        modifier = Modifier
            .offset {
                horizontalOffset()
            }
            .fillMaxWidth()
            .height(lineWidth())
            .clip(CircleShape)
    )
}

@Composable
private fun BoxScope.PlayerImage(
    playerChar: Char?
) {
    Image(
        painter = painterResource(
            id = when (playerChar) {
                'X' -> R.drawable.ic_tic_tac_toe_x
                'O' -> R.drawable.ic_tic_tac_toe_o
                else -> R.drawable.transparent
            }
        ),
        colorFilter = ColorFilter.tint(
            if (playerChar == 'X') MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        ),
        contentDescription = null,
        modifier = Modifier
            .matchParentSize()
    )
}

@Preview
@Composable
fun previewBoard() {
    TicTacToeBoard(
        board = board,
        onClick = { row, col -> },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1f)
    )
}

val board: Array<Array<Char?>> = arrayOf(
    arrayOf('X', null, null),
    arrayOf(null, 'O', null),
    arrayOf('O', null, null),
)