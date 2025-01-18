
# Solving TicTacToe using Monte Carlo Tree Search or Minimax optimized with alpha beta pruning

**Work in Progress**

## Overview

This project is an implementation of the classic Tic Tac Toe game, showcasing the power of AI through the Monte Carlo Tree Search (MCTS) and Minimax algorithm with Alpha-Beta pruning. This project demonstrates efficient decision-making and strategic gameplay, offering a formidable AI opponent.

The project is built for Android using Jetpack Compose, ensuring a modern, declarative UI framework for a smooth and interactive gaming experience.
## Branches

-   The **Minimax** implementation can be found in the `minimax` branch.
    
-   The **MCTS** implementation can be found in the `mcts` branch.
## Algorithm

### Monte Carlo Tree Search (MCTS)
MCTS is a probabilistic search algorithm that combines random sampling with a strategic tree-building process. It balances exploration and exploitation to find optimal moves without requiring exhaustive exploration of the game tree.

-   **Selection**: Navigate the tree to choose the most promising node.

-   **Expansion**: Add new child nodes to the tree.

-   **Simulation**: Perform random simulations to evaluate potential outcomes.

-   **Backpropagation**: Update the tree with the simulation results to improve decision-making.

## Images

## Screenshots
<div style="display: flex; gap: 10px;">
    <img src="assets/screen_shot_1.png" width="300" alt="Screenshot 1">
    <img src="assets/screen_shot_2.png" width="300" alt="Screenshot 2">
    <img src="assets/screen_shot_3.png" width="300" alt="Screenshot 3">
</div>

## Video
<video width="640" height="360" controls>
  <source src="assets/minimax_alpha_beta.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>
