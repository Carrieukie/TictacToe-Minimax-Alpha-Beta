package com.example.minimaxtictactoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimaxtictactoe.models.Player
import com.example.minimaxtictactoe.persistence.UserPreferencesRepository
import com.example.minimaxtictactoe.state.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class TicTacToeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _ticTacToeState = MutableStateFlow(GameState())
    val ticTacToeState: StateFlow<GameState> = _ticTacToeState

    fun updateField(x: Int, y: Int, player: Player) {
        //get the board
        val currentField = _ticTacToeState.value.field

        //if there's a winner stop the game
        val winner = _ticTacToeState.value.winner
        if (winner != null) {
            return
        }

        //update the board
        currentField[y][x] = player.character

        //update game state
        _ticTacToeState.value = _ticTacToeState.value.copy(
            field = currentField,
            num = Random.nextInt(1, 100),

            )

        // check the new game state if it has a winner
        val evaluateWinner = evaluateWinner(_ticTacToeState.value.field)
        val message =
            if (evaluateWinner == "X") "You win" else if (evaluateWinner == "O") " Ai wins" else if (evaluateWinner == "tie") "tie" else null

        _ticTacToeState.value = _ticTacToeState.value.copy(
            winner = evaluateWinner,
            message = message,
            num = Random.nextInt(),
            aiWins = incrementAiWinCount(_ticTacToeState.value, evaluateWinner),
            humanWins = incrementHumanWinsCount(_ticTacToeState.value, evaluateWinner),
            gamesTied = incrementGamesTied(_ticTacToeState.value, evaluateWinner)
        )

        if (player is Player.Human) {
            nextTurn(_ticTacToeState.value)
        }
    }

    private fun incrementGamesTied(gameState: GameState, evaluateWinner: String?): Int {
        return if (evaluateWinner == "tie") {
            gameState.gamesTied + 1
        } else {
            gameState.gamesTied
        }
    }

    private fun incrementHumanWinsCount(gameState: GameState, evaluateWinner: String?): Int {
        return if (evaluateWinner == "X") {
            gameState.humanWins + 1
        } else {
            gameState.humanWins
        }
    }

    private fun incrementAiWinCount(gameState: GameState, evaluateWinner: String?): Int {
        return if (evaluateWinner == "O") {
            gameState.aiWins + 1
        } else {
            gameState.aiWins
        }
    }

    private fun nextTurn(gameState: GameState) {

        val board = gameState.field
        var bestScore = Int.MIN_VALUE
        var bestMove = Pair(0, 0)
        for (i in board.indices) {
            for (j in 0 until board[0].size) {
                if (board[i][j] == null) {
                    board[i][j] = Player.AI.character
                    val score = minimax(board, 0, false)
                    board[i][j] = null
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = Pair(i, j)
                    }
                }
            }
        }
        updateField(bestMove.second, bestMove.first, Player.AI)
    }

    fun resetGame() {
        _ticTacToeState.value = _ticTacToeState.value.copy(
            field = GameState.emptyField(),
            round = _ticTacToeState.value.round + 1,
            winner = null,
            num = Random.nextInt(),
            message = null
        )
    }

    private fun minimax(board: Array<Array<Char?>>, depth: Int, isMaximizing: Boolean): Int {
        val evaluateWinner = evaluateWinner(board)
        if (evaluateWinner != null) {
            val score = scores[evaluateWinner]
            return score!!
        }

        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                for (j in 0 until board[0].size) {
                    if (board[i][j] == null) {
                        board[i][j] = Player.AI.character
                        val score = minimax(board, depth + 1, false)
                        board[i][j] = null
                        bestScore = max(score, bestScore)
                    }
                }
            }
            return bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                for (j in 0 until board[0].size) {
                    if (board[i][j] == null) {
                        board[i][j] = Player.Human.character
                        val score = minimax(board, depth + 1, true)
                        board[i][j] = null
                        bestScore = min(score, bestScore)
                    }
                }
            }
            return bestScore
        }

    }

    private fun evaluateWinner(board: Array<Array<Char?>>): String? {
        var winner: String? = null
        //horizontal
        for (i in board.indices) {
            if (equals3(board[i][0], board[i][1], board[i][2])) {
                winner = board[i][0].toString()
            }
        }

        // vertical
        for (i in board.indices) {
            if (equals3(board[0][i], board[1][i], board[2][i])) {
                winner = board[0][i].toString()
            }
        }

        // Diagonal
        if (equals3(board[0][0], board[1][1], board[2][2])) {
            winner = board[0][0].toString()
        }

        if (equals3(board[0][2], board[1][1], board[2][0])) {
            winner = board[0][2].toString()
        }

        // tie
        var openSpots = 0
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == null) {
                    openSpots++
                }
            }
        }
        return if (winner == null && openSpots == 0) {
            "tie"
        } else {
            winner
        }
    }

    private fun equals3(a: Char?, b: Char?, c: Char?): Boolean {
        return a == b && b == c && a != null
    }

    private val scores = mapOf(
        "X" to -1,
        "O" to 1,
        "tie" to 0
    )

    val themeStream: Flow<Int> get() = userPreferencesRepository.getTheme
    fun setTheme(themeValue: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveTheme(themeValue = themeValue)
        }
    }

}