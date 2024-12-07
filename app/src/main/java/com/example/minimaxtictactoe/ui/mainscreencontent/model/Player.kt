package com.example.minimaxtictactoe.ui.mainscreencontent.model

sealed class Player(
    val character: Char
){
    object Human : Player('X')
    object AI : Player('O')
}
