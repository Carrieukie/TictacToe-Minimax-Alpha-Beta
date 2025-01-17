package com.example.minimaxtictactoe.mcts

abstract class Node {
    /**
     * All possible successors of this board state
     */
    abstract fun findChildren(): Set<Node>

    /**
     * Random successor of this board state (for more efficient simulation)
     */
    abstract fun findRandomChild(): Node?

    /**
     * Returns true if the node has no children
     */
    abstract fun isTerminal(): Boolean

    /**
     * Assumes `this` is a terminal node. 1 = win, 0 = loss, 0.5 = tie, etc.
     */
    abstract fun reward(): Double

    /**
     * Nodes must be hashable
     */
    override abstract fun hashCode(): Int

    /**
     * Nodes must be comparable
     */
    override abstract fun equals(other: Any?): Boolean
}