package com.example.tictactoeonline

import kotlin.random.Random

class GameModel(gameStatus: GameStatus) {
    var gameId: String = "-1"
    var filledPos: MutableList<String> = mutableListOf("","","","","","","","","")
    var winner: String = ""
    var gameStatus: GameStatus = GameStatus.CREATED
    var currentTurn: String = if (Random.nextBoolean()) "X" else "O"
    var score: Double = 0.0
}

enum class GameStatus {
    DEFAULT ,
    CREATED,
    JOINED,
    INPROGRESS,
    FINISHED
}
