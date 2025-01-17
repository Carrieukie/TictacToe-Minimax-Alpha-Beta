package impl_two

import com.example.minimaxtictactoe.mcts.Node

data class Board(
    val prevBoard: Board? = null,
    val position: Map<Pair<Int, Int>, String>,
    val currentPlayer: String = PLAYER_X,
    val size: Int = 3,
): Node() {

    constructor(size: Int, currentPlayer: String = PLAYER_X) : this(
        prevBoard = null,
        position = initBoard(size),
        currentPlayer = currentPlayer,
        size = size,
    )
    /**
     * Generates all possible child nodes (next possible game states) from the current state.
     *
     * @return a set of unique child nodes representing all possible next game states.
     */
    override fun findChildren(): Set<Node> {
        return generateNextPossibleStates().toSet()
    }

    /**
     * Selects a random child node (next possible game state) from the current state.
     *
     * @return a random child node if the current state is not terminal and has children,
     *         or null if the state is terminal or has no children.
     */
    override fun findRandomChild(): Node? {
        if (isTerminal()) return null
        val children = generateNextPossibleStates()
        return if (children.isEmpty()) null else children.random()
    }

    /**
     * Checks if the current state is terminal.
     * A state is terminal if the game ends in a draw or a win.
     *
     * @return true if the state is terminal, false otherwise.
     */
    override fun isTerminal(): Boolean {
        return isDraw() || isWin()
    }

    /**
     * Calculates the reward for the current state.
     * This function should only be called on terminal states.
     *
     * @return the reward value:
     *         - 1.0 if the current player wins,
     *         - 0.0 if the opponent wins,
     *         - 0.5 if the game ends in a tie.
     * @throws IllegalStateException if called on a non-terminal state.
     */
    override fun reward(): Double {
        require(isTerminal()) { "Reward called on non-terminal board: $this" }

        val winReward = 1.0
        val lossReward = -1.0
        val tieReward = 0.0

        val winner = getWinner()
        val opponent = if (currentPlayer == "x") "o" else "x"

        return when (winner) {
            currentPlayer -> winReward   // Current player wins
            opponent -> lossReward       // Opponent wins
            null -> tieReward            // Tie
            else -> throw IllegalStateException("Unexpected winner value: $winner")
        }
    }

    /**
     * Generates a list of all possible next game states from the current state.
     *
     * @return a list of new board states created by making valid moves on the current board.
     */
    private fun generateNextPossibleStates(): List<Board> {
        return position.filter { it.value == EMPTY_SQUARE }.map { (key, _) ->
            val (row, col) = key
            makeMove(row, col)
        }
    }

    /**
     * Checks if the game is a draw.
     * A draw occurs when there are no empty squares left on the board.
     *
     * @return `true` if the board has no empty squares, otherwise `false`.
     */
    fun isDraw(): Boolean = position.values.none { it == EMPTY_SQUARE }

    /**
     * Determines if the game has a winner.
     *
     * @return `true` if a winner exists, otherwise `false`.
     */
    fun isWin(): Boolean = getWinner() != null

    /**
     * Identifies the winner of the game, if any.
     *
     * @return The winning player symbol (`"x"` or `"o"`), or `null` if no winner exists.
     */
    fun getWinner(): String? {
        for (row in 0 until size) {
            for (col in 0 until size) {
                val player = position[row to col]
                if (player == EMPTY_SQUARE) continue

                // Check row, column, and diagonals from the current position
                if (checkRowWinFromPosition(row, col) != null ||
                    checkColWinFromPosition(row, col) != null ||
                    checkDiagonalWinFromPosition(row, col) != null
                ) {
                    return player
                }
            }
        }
        return null
    }

    /**
     * Checks if there is a winning line in the row containing the given position.
     *
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @return The winning player symbol (`"x"` or `"o"`), or `null` if no win exists in the row.
     */
    fun checkRowWinFromPosition(row: Int, col: Int): String? {
        val player = position[row to col] ?: return null
        if (player == EMPTY_SQUARE) return null

        for (c in col until size + col) {
            if (position[row to c] != player) return null
        }
        return player
    }

    /**
     * Checks if there is a winning line in the column containing the given position.
     *
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @return The winning player symbol (`"x"` or `"o"`), or `null` if no win exists in the column.
     */
    fun checkColWinFromPosition(row: Int, col: Int): String? {
        val player = position[row to col] ?: return null
        if (player == EMPTY_SQUARE) return null

        for (r in row until size + row) {
            if (position[r to col] != player) return null
        }
        return player
    }

    /**
     * Checks if there is a winning line on either diagonal containing the given position.
     *
     * @param row The row index of the starting position.
     * @param col The column index of the starting position.
     * @return The winning player symbol (`"x"` or `"o"`), or `null` if no win exists on either diagonal.
     */
    fun checkDiagonalWinFromPosition(row: Int, col: Int): String? {
        val player = position[row to col] ?: return null
        if (player == EMPTY_SQUARE) return null

        // Check the main diagonal if the position is on it
        if (row == col) {
            if ((0 until size).all { i -> position[i to i] == player }) {
                return player
            }
        }

        // Check the anti-diagonal if the position is on it
        if (row + col == size - 1) {
            if ((0 until size).all { i -> position[i to size - 1 - i] == player }) {
                return player
            }
        }

        // Not a winning move
        return null
    }


    fun makeMove(row: Int, col: Int): Board {
        return copy(
            // Keep a reference to the current board as the previous state
            prevBoard = this,

            position = position.toMutableMap().apply {
                this[row to col] = currentPlayer
            },
            // Toggle the current player
            currentPlayer = if (currentPlayer == PLAYER_X) PLAYER_O else PLAYER_X
        )
    }

    override fun toString(): String {
        val boardStr = buildString {
            for (col in 0 until size) {
                for (row in 0 until size) {
                    append(" ${position[row to col] ?: EMPTY_SQUARE}")
                }
                append("\n")
            }
        }
        val turn =  "\"$currentPlayer\" to move:"
        return "\n--------------\n $turn\n--------------\n\n$boardStr $position"
    }

    fun toBoardArray(): Array<Array<String?>> {
        val boardArray = Array(size) { arrayOfNulls<String>(size) }
        for (col in 0 until size) {
            for (row in 0 until size) {
                boardArray[col][row] =  if (position[row to col] == EMPTY_SQUARE) null else position[row to col]
            }
        }
        return boardArray
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false

        if (position != other.position) return false
        if (currentPlayer != other.currentPlayer) return false

        return true
    }

    override fun hashCode(): Int {
        return position.hashCode() + currentPlayer.hashCode()
    }

    companion object {
        const val PLAYER_X = "x"
        const val PLAYER_O = "o"
        const val EMPTY_SQUARE = "."

        private fun initBoard(size: Int): MutableMap<Pair<Int, Int>, String> {
            val position = mutableMapOf<Pair<Int, Int>, String>()
            for (row in 0 until size) {
                for (col in 0 until size) {
                    position[col to row] = EMPTY_SQUARE
                }
            }
            return position
        }
    }
}