package com.example.minimaxtictactoe.ui.mainscreencontent.model

data class GameState(
    val field: Array<Array<Char?>> = emptyField(),
    val num: Int = 0,
    val players: Array<Player> = arrayOf(Player.Human, Player.AI),
    val winner: String? = null,
    val message: String ?= null,
    val round: Int = 1,
    val humanWins:Int = 0,
    val aiWins: Int = 0,
    val gamesTied: Int = 0,
    val currentPlayerIndex: Int = 0,
    val winningPlayer: Player? = null
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!field.contentDeepEquals(other.field)) return false
        if (num != other.num) return false
        if (!players.contentEquals(other.players)) return false
        if (winner != other.winner) return false
        if (message != other.message) return false
        if (round != other.round) return false
        if (humanWins != other.humanWins) return false
        if (aiWins != other.aiWins) return false
        if (gamesTied != other.gamesTied) return false
        if (currentPlayerIndex != other.currentPlayerIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.contentDeepHashCode()
        result = 31 * result + num
        result = 31 * result + players.contentHashCode()
        result = 31 * result + (winner?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + round
        result = 31 * result + humanWins
        result = 31 * result + aiWins
        result = 31 * result + gamesTied
        result = 31 * result + currentPlayerIndex
        return result
    }

}