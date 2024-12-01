package com.example.minimaxtictactoe.ui.mainscreencontent.model

sealed class TabItem(
    val icon: Int,
    val playerName: String,
    val score: Int
){
    class You(icon: Int,  playerName: String, score: Int): TabItem(icon = icon, playerName = playerName, score = score)
    class Draw(icon: Int,  playerName: String, score: Int): TabItem(icon = icon, playerName = playerName, score = score)
    class AI(icon: Int,  playerName: String, score: Int): TabItem(icon = icon, playerName = playerName, score = score)
}
