package com.example.minimaxtictactoe.ui.mainscreencontent.model

import com.example.minimaxtictactoe.mcts.Board


data class GameState(
    val board: Board = Board(3),
    val num: Int = 0,
    val winner: String? = null,
    val message: String ?= null,
    val round: Int = 1,
    val humanWins:Int = 0,
    val aiWins: Int = 0,
    val gamesTied: Int = 0,
    val currentPlayerIndex: Int = 0,
    val winningPlayer: String? = null
) {
    companion object {
        fun emptyField(): Array<Array<Char?>> {
            return arrayOf(
                arrayOf(null, null, null),
                arrayOf(null, null, null),
                arrayOf(null, null, null),
            )
        }

    }

}