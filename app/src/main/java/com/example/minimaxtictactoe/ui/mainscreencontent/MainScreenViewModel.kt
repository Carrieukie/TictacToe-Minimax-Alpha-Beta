package com.example.minimaxtictactoe.ui.mainscreencontent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimaxtictactoe.mcts.MCTS
import com.example.minimaxtictactoe.persistence.UserPreferencesRepository
import com.example.minimaxtictactoe.ui.mainscreencontent.model.GameState
import com.example.minimaxtictactoe.ui.mainscreencontent.model.TicTacToeEvent
import com.example.minimaxtictactoe.mcts.Board
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

class MainScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    val themeStream: Flow<Int> get() = userPreferencesRepository.getTheme

    private val _ticTacToeState = MutableStateFlow(GameState())
    val ticTacToeState: StateFlow<GameState> = _ticTacToeState

    private val _ticTacToeEvents = Channel<TicTacToeEvent>(Channel.BUFFERED)
    val ticTacToeEventsFlow: Flow<TicTacToeEvent> = _ticTacToeEvents.receiveAsFlow()

    fun onEvent(event: TicTacToeEvent) {
        when (event) {
            is TicTacToeEvent.UpdateField -> updateField(event.x, event.y)
            is TicTacToeEvent.ResetGame -> resetGameState()
            is TicTacToeEvent.FlipDrawerState -> _ticTacToeEvents.trySend(TicTacToeEvent.FlipDrawerState)
            is TicTacToeEvent.ShowToast -> _ticTacToeEvents.trySend(event)
        }
    }

    private fun updateField(row: Int, col: Int) {
        val value = _ticTacToeState.value.board.position[row to col]
        if (value != Board.EMPTY_SQUARE) {
            println("Illegal move! The square is already occupied.")
            _ticTacToeEvents.trySend(TicTacToeEvent.ShowToast("Illegal move! The square is already occupied."))
            return
        }

        val newBoard = _ticTacToeState.value.board.makeMove(row, col)

        val winner = if (newBoard.isDraw()) "tie" else newBoard.getWinner()

        val message = when (winner) {
            "x" -> "You win"
            "o" -> "AI wins"
            "tie" -> "Tie"
            else -> null
        }

        val currentPlayerIndex = when (winner) {
            "tie" -> 1
            "x" -> 0
            "o" -> 2
            else -> if (newBoard.currentPlayer == "x") 0 else 2
        }

        updateGameState {
            copy(
                board = newBoard,
                winner = winner,
                message = message,
                currentPlayerIndex = currentPlayerIndex,
                aiWins = incrementIf(aiWins, winner == "o"),
                humanWins = incrementIf(humanWins, winner == "x"),
                gamesTied = incrementIf(gamesTied, winner == "tie"),
            )
        }
        if (!newBoard.isTerminal()) {
            viewModelScope.launch(Dispatchers.IO) {
                makeAIMove()
            }
        }
    }


    private fun makeAIMove() {
        val currentState = _ticTacToeState.value

         val mcts: MCTS = MCTS(sqrt(12.0))
        repeat(10000) { mcts.doRollout(currentState.board) }
        val newBoard = mcts.choose(currentState.board) as Board

        Log.e("MainScreenViewModel", "updateField: newBoard: $newBoard")

        val winner = newBoard.getWinner()
        val message = when (winner) {
            "x" -> "You win"
            "o" -> "AI wins"
            "tie" -> "Tie"
            else -> null
        }


        val currentPlayerIndex = when (winner) {
            "tie" -> 1
            "x" -> 0
            "o" -> 2
            else -> if (newBoard.currentPlayer == "x") 0 else 2
        }

        updateGameState {
            copy(
                board = newBoard,
                winner = winner,
                message = message,
                currentPlayerIndex =currentPlayerIndex,
                aiWins = incrementIf(aiWins, winner == "o"),
                humanWins = incrementIf(humanWins, winner == "x"),
                gamesTied = incrementIf(gamesTied, winner == "tie"),
            )
        }
    }


    private fun resetGameState() {
        updateGameState {
            copy(
                board = Board(3),
                round = round + 1,
                winner = null,
                num = Random.nextInt(),
                message = null,
                currentPlayerIndex = 0
            )
        }
    }


    private fun incrementIf(count: Int, condition: Boolean) = if (condition) count + 1 else count


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


}