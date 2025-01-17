import com.example.minimaxtictactoe.mcts.Board
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BoardTest {

    @Test
    fun `isWin returns true for first horizontal win`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to "x", 0 to 2 to "x",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected horizontal win for player x.")
    }

    @Test
    fun `isWin returns true for second horizontal win`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to ".", 0 to 2 to "x",
            1 to 0 to "x", 1 to 1 to "x", 1 to 2 to "x",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected horizontal win for player x.")
    }

    @Test
    fun `isWin returns true for third horizontal win`() {
        val position = mutableMapOf(
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to ".",
            0 to 0 to "x", 0 to 1 to "x", 0 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected horizontal win for player x.")
    }

    @Test
    fun `isWin returns true for first vertical win`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to ".", 0 to 2 to "o",
            1 to 0 to "x", 1 to 1 to "o", 1 to 2 to "o",
            2 to 0 to "x", 2 to 1 to ".", 2 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected vertical win for player x.")
    }

    @Test
    fun `isWin returns true for second vertical win`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to ".",
            1 to 0 to "x", 1 to 1 to "o", 1 to 2 to ".",
            2 to 0 to "o", 2 to 1 to "o", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected vertical win for player o.")
    }

    @Test
    fun `isWin returns true for third vertical win`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to ".", 0 to 2 to "o",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to "o",
            2 to 0 to "o", 2 to 1 to ".", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected vertical win for player o.")
    }

    @Test
    fun `isWin returns true for first diagonal win`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to "x", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkDiagonalWinFromPosition(0, 0), "Expected win for first diagonal.")
        assertTrue(board.isWin(), "Expected diagonal win for player x.")
    }

    @Test
    fun `isWin returns true for second diagonal win`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to ".", 0 to 2 to "x",
            1 to 0 to ".", 1 to 1 to "x", 1 to 2 to ".",
            2 to 0 to "x", 2 to 1 to ".", 2 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected diagonal win for player x.")
    }

    @Test
    fun `isWin returns true for a random configured win`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to "o", 0 to 2 to "x",
            1 to 0 to "o", 1 to 1 to "x", 1 to 2 to "o",
            2 to 0 to "x", 2 to 1 to "o", 2 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected a win for player x.")
    }

    @Test
    fun `isWin returns false for a draw`() {
        val position = mutableMapOf(
            0 to 0 to "x", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "o", 1 to 1 to "x", 1 to 2 to "o",
            2 to 0 to "x", 2 to 1 to "o", 2 to 2 to "x"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected a win for player x.")
    }

    @Test
    fun `isWin returns true for a win for o`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "x",
            1 to 0 to "o", 1 to 1 to "o", 1 to 2 to "x",
            2 to 0 to "x", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertTrue(board.isWin(), "Expected a win for player x.")
    }

    @Test
    fun `isWin returns false for incomplete board`() {
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertFalse(board.isWin(), "Expected no win on an empty board.")
    }

    @Test
    fun `checkRowFromPosition returns true for board row1 pos 0,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "x", 1 to 1 to "x", 1 to 2 to "x",
            2 to 0 to "", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkRowWinFromPosition(0, 0), "Expected win on first row.")
    }

    @Test
    fun `checkRowFromPosition returns true for board row2 1,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "x", 1 to 1 to "x", 1 to 2 to "x",
            2 to 0 to "", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkRowWinFromPosition(1, 0), "Expected win on second row.")
    }

    @Test
    fun `checkRowFromPosition returns true for board row3 2,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "x", 1 to 1 to "x", 1 to 2 to "x",
            2 to 0 to "o", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkRowWinFromPosition(2, 0), "Expected win on third row.")
    }

    @Test
    fun `checkRowFromPosition returns false for board row3 2,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "x", 1 to 1 to "x", 1 to 2 to "x",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNull(board.checkRowWinFromPosition(2, 0), "Expected no win on an empty board.")
    }

    @Test
    fun `checkRowFromPosition returns false for board col1 2,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "o", 1 to 1 to "o", 1 to 2 to "o",
            2 to 0 to "o", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkColWinFromPosition(0, 0), "Expected no win on an empty board.")
    }

    @Test
    fun `checkRowFromPosition returns false for board col2 2,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "o", 1 to 1 to "o", 1 to 2 to "o",
            2 to 0 to "o", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkColWinFromPosition(0, 1), "Expected no win on an empty board.")
    }

    @Test
    fun `checkRowFromPosition returns false for board col3 2,0`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "o", 0 to 2 to "o",
            1 to 0 to "o", 1 to 1 to "o", 1 to 2 to "o",
            2 to 0 to "o", 2 to 1 to "o", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkColWinFromPosition(0, 2), "Expected no win on an empty board.")
        assertNull(board.checkColWinFromPosition(1, 2), "Expected no win on an empty board.")
    }

    @Test
    fun `checkRowFromPosition returns false for empty board`() {
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNull(board.checkColWinFromPosition(0, 0), "Expected no win on an empty board.")
        assertNull(board.checkColWinFromPosition(0, 1), "Expected no win on an empty board.")
        assertNull(board.checkColWinFromPosition(0, 2), "Expected no win on an empty board.")
    }


    @Test
    fun `checkDiagonalFromPosition returns true for diagonal wins`() {
        val position = mutableMapOf(
            0 to 0 to "o", 0 to 1 to "x", 0 to 2 to "o",
            1 to 0 to ".", 1 to 1 to "o", 1 to 2 to ".",
            2 to 0 to "o", 2 to 1 to ".", 2 to 2 to "o"
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNotNull(board.checkDiagonalWinFromPosition(0, 0), "Expected win for first diagonal.")
        assertNotNull(board.checkDiagonalWinFromPosition(2, 0), "Expected no win")
        assertNull(board.checkDiagonalWinFromPosition(0, 1), "Expected no win on an empty board.")
    }


    @Test
    fun `checkDiagonalFromPosition returns false for empty board`() {
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertNull(board.checkDiagonalWinFromPosition(0, 0), "Expected no win on an empty board.")
        assertNull(board.checkDiagonalWinFromPosition(2, 0), "Expected no win on an empty board.")
        assertNull(board.checkDiagonalWinFromPosition(0, 1), "Expected no win on an empty board.")
    }


    @Test
    fun `makeMove returns a new board with made move`() {
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        val newBoard = board.makeMove(0, 0)
        assertEquals("x", newBoard.position[0 to 0], "Expected player x to make a move.")
    }

    @Test
    fun `makeMove changes the current player`() {
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)
        assertEquals("o", board.makeMove(0, 0).also { println(it) }.currentPlayer, "Expected player x to make a move.")
    }

    @Test
    fun `makeMove properly maintains previous board state`() {
        // Make a move and check the previous state
        val position = mutableMapOf(
            0 to 0 to ".", 0 to 1 to ".", 0 to 2 to ".",
            1 to 0 to ".", 1 to 1 to ".", 1 to 2 to ".",
            2 to 0 to ".", 2 to 1 to ".", 2 to 2 to "."
        )
        val board = Board(position = position, currentPlayer = "x", size = 3)

        val newBoard = board.makeMove(0, 0)
        // Ensure that prevBoard is not null
        assertTrue { newBoard.prevBoard != null }
        assertEquals(board.position, newBoard.prevBoard?.position)
    }

}
