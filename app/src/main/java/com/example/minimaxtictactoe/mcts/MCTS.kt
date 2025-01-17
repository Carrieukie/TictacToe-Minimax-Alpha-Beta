package com.example.minimaxtictactoe.mcts
import kotlin.Throws
import kotlin.math.ln
import kotlin.math.sqrt

class MCTS(private val explorationWeight: Double = 0.5) {
    private val Q = mutableMapOf<Node, Double>() // total reward of each node
    private val N = mutableMapOf<Node, Int>() // total visit count for each node
    private val children = mutableMapOf<Node, Set<Node>>() // children of each node

    @Throws(RuntimeException::class)
    fun choose(node: Node): Node {
        if (node.isTerminal()) {
            throw RuntimeException("choose called on terminal node $node")
        }

        if (!children.containsKey(node)) {
            return node.findRandomChild() ?: throw RuntimeException("No children found for node $node")
        }

        return children[node]?.maxByOrNull { score(it) } ?: throw RuntimeException("No valid children")
    }

    private fun score(node: Node): Double {
        val visits = N[node] ?: 0
        return if (visits == 0) Double.NEGATIVE_INFINITY else (Q[node] ?: 0.0) / visits
    }

    fun doRollout(node: Node) {
        val path = select(node)
        val leaf = path.last()
        expand(leaf)
        val reward = simulate(leaf)
        backpropagate(path, reward)
    }

    private fun select(node: Node): List<Node> {
        val path = mutableListOf<Node>()
        var current = node

        while (true) {
            path.add(current)
            if (!children.containsKey(current) || children[current]!!.isEmpty()) {
                return path
            }
            val unexplored = children[current]!! - children.keys
            if (unexplored.isNotEmpty()) {
                path.add(unexplored.first())
                return path
            }
            current = uctSelect(current)
        }
    }

    private fun expand(node: Node) {
        if (children.containsKey(node)) return
        children[node] = node.findChildren()
    }

    @Throws(RuntimeException::class)
    private fun simulate(node: Node): Double {
        var current = node
        var invertReward = true

        while (true) {
            if (current.isTerminal()) {
                val reward = current.reward()
                return if (invertReward) 1 - reward else reward
            }
            current = current.findRandomChild() ?: throw RuntimeException("No random child found")
            invertReward = !invertReward
        }
    }

    private fun backpropagate(path: List<Node>, reward: Double) {
        var currentReward = reward

        for (node in path.asReversed()) {
            N[node] = (N[node] ?: 0) + 1
            Q[node] = (Q[node] ?: 0.0) + currentReward
            currentReward = 1 - currentReward
        }
    }

    @Throws(RuntimeException::class)
    private fun uctSelect(node: Node): Node {
        val logNVertex = ln((N[node] ?: 1).toDouble())

        return children[node]!!.maxByOrNull { child ->
            val visits = N[child] ?: 0
            val qValue = Q[child] ?: 0.0
            qValue / visits + explorationWeight * sqrt(logNVertex / visits)
        } ?: throw RuntimeException("No valid UCT selection for node $node")
    }
}
