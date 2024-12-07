package com.example.minimaxtictactoe.ui.mainscreencontent.model

sealed interface TicTacToeEvent {
    data class UpdateField(val x: Int,val y: Int, val player: Player): TicTacToeEvent
    data object ResetGame: TicTacToeEvent
    data object FlipDrawerState: TicTacToeEvent
}