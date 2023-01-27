package com.example.minimaxtictactoe.models

sealed class Player(
    val character: Char
){
    object Human : Player('X')
    object AI : Player('O')
}
