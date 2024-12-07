package com.example.minimaxtictactoe.ui.mainscreencontent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimaxtictactoe.ui.mainscreencontent.model.Player
import com.example.minimaxtictactoe.persistence.UserPreferencesRepository
import com.example.minimaxtictactoe.ui.mainscreencontent.model.GameState
import com.example.minimaxtictactoe.ui.mainscreencontent.model.TicTacToeEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val themeStream: Flow<Int> get() = userPreferencesRepository.getTheme

    private val _ticTacToeState = MutableStateFlow(GameState())
    val ticTacToeState: StateFlow<GameState> = _ticTacToeState

    private val _ticTacToeEvents = Channel<TicTacToeEvent>(Channel.BUFFERED)
    val ticTacToeEventsFlow: Flow<TicTacToeEvent> = _ticTacToeEvents.receiveAsFlow()

    fun onEvent(event: TicTacToeEvent) {
        when (event) {
            is TicTacToeEvent.UpdateField -> updateField(event.x, event.y, event.player)
            is TicTacToeEvent.ResetGame -> resetGameState()
            TicTacToeEvent.FlipDrawerState -> _ticTacToeEvents.trySend(TicTacToeEvent.FlipDrawerState)
        }
    }

    private fun updateField(x: Int, y: Int, player: Player) {
        val currentState = _ticTacToeState.value

        // Stop if the game already has a winner or the cell is occupied
        if (currentState.winner != null || currentState.field[y][x] != null) return

        // Update the board
        val updatedField = currentState.field.map { it.clone() }.toTypedArray()
        updatedField[y][x] = player.character

        // Check for a winner or tie
        val winner = evaluateWinner(updatedField)
        val message = when (winner) {
            "X" -> "You win"
            "O" -> "AI wins"
            "tie" -> "Tie"
            else -> null
        }

        // Update game state
        updateGameState {
            copy(
                field = updatedField,
                winner = winner,
                message = message,
                aiWins = incrementIf(aiWins, winner == "O"),
                humanWins = incrementIf(humanWins, winner == "X"),
                gamesTied = incrementIf(gamesTied, winner == "tie"),
                currentPlayerIndex = when {
                    winner == "tie" -> 1
                    winner == "X" -> 0
                    winner == "O" -> 2
                    player == Player.Human -> 2
                    player == Player.AI -> 0
                    else -> 1
                }
            )
        }

        viewModelScope.launch {
            delay(Random.nextLong(200,700))
            // Trigger AI move if it's the human's turn
            if (player is Player.Human && winner == null) {
                makeAIMove()
            }
        }
    }


    private fun makeAIMove() {
        // Get the current state of the board
        val board = _ticTacToeState.value.field

        // Initialize variables to track the best score and move
        var bestScore = Int.MIN_VALUE
        var bestMove = Pair(0, 0)

        // Loop through each cell of the board to find all possible moves
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j] == null) {
                    // Simulate the AI making a move in the empty spot
                    board[i][j] = Player.AI.character

                    // Use the minimax algorithm to evaluate the move
                    val score = minimax(board, 0, false)

                    // Undo the move (backtrack)
                    board[i][j] = null

                    // Update the best score and move if the current move is better
                    if (score > bestScore) {
                        bestScore = score
                        bestMove = Pair(i, j)
                    }
                }
            }
        }

        // Execute the best move found by the minimax algorithm
        updateField(bestMove.second, bestMove.first, Player.AI)
    }


    private fun resetGameState() {
        updateGameState {
            copy(
                field = GameState.emptyField(),
                round = round + 1,
                winner = null,
                num = Random.nextInt(),
                message = null,
                currentPlayerIndex = 0
            )
        }
    }

    private fun minimax(
        board: Array<Array<Char?>>,
        depth: Int,
        isMaximizing: Boolean,
        alpha: Int = Int.MIN_VALUE,
        beta: Int = Int.MAX_VALUE
    ): Int {
        // Evaluate the current state of the board
        val winner = evaluateWinner(board)
        if (winner != null) {
            // If there's a winner or a tie, return the corresponding score
            return scores[winner] ?: 0
        }

        // Initialize alpha and beta for pruning
        var alphaVar = alpha
        var betaVar = beta

        // Maximizing player (AI)
        if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j] == null) {
                        // Make a move
                        board[i][j] = Player.AI.character

                        // Recursive call to minimax
                        val score = minimax(board, depth + 1, false, alphaVar, betaVar)

                        // Undo the move
                        board[i][j] = null

                        // Update the best score
                        bestScore = max(score, bestScore)

                        // Update alpha
                        alphaVar = max(alphaVar, bestScore)

                        // Alpha-Beta pruning
                        if (betaVar <= alphaVar) break
                    }
                }
            }
            return bestScore
        } else { // Minimizing player (Human)
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                for (j in board[i].indices) {
                    if (board[i][j] == null) {
                        // Make a move
                        board[i][j] = Player.Human.character

                        // Recursive call to minimax
                        val score = minimax(board, depth + 1, true, alphaVar, betaVar)

                        // Undo the move
                        board[i][j] = null

                        // Update the best score
                        bestScore = min(score, bestScore)

                        // Update beta
                        betaVar = min(betaVar, bestScore)

                        // Alpha-Beta pruning
                        if (betaVar <= alphaVar) break
                    }
                }
            }
            return bestScore
        }
    }


    private fun evaluateWinner(board: Array<Array<Char?>>): String? {
        // Check rows, columns, and diagonals
        for (i in board.indices) {
            if (equals3(board[i][0], board[i][1], board[i][2])) return board[i][0].toString()
            if (equals3(board[0][i], board[1][i], board[2][i])) return board[0][i].toString()
        }
        if (equals3(board[0][0], board[1][1], board[2][2])) return board[0][0].toString()
        if (equals3(board[0][2], board[1][1], board[2][0])) return board[0][2].toString()

        // Check for tie
        return if (board.all { row -> row.all { it != null } }) "tie" else null
    }

    private fun incrementIf(count: Int, condition: Boolean) = if (condition) count + 1 else count

    private fun equals3(a: Char?, b: Char?, c: Char?): Boolean {
        return a == b && b == c && a != null
    }

    private fun updateGameState(reducer: GameState.() -> GameState) {
        _ticTacToeState.update {
            it.reducer()
        }
    }

    fun setTheme(themeValue: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveTheme(themeValue = themeValue)
        }
    }

    companion object {
        private val scores = mapOf(
            "X" to -1,
            "O" to 1,
            "tie" to 0
        )
    }

}