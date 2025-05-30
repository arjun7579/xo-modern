package com.example.tictactoeonline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tictactoeonline.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private var gameModel: GameModel? = null
    private lateinit var binding: ActivityGameBinding

    // Queues to track last 3 moves for each player
    private val xQueue = ArrayDeque<Int>(3)
    private val oQueue = ArrayDeque<Int>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListeners for the 9 game buttons
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)
        binding.btn9.setOnClickListener(this)

        binding.startGameBtn.setOnClickListener {
            startGame()
        }

        // Observe game data changes
        GameData.gameModel.observe(this) {
            gameModel = it
            set_UI()
        }
    }

    private fun set_UI() {
        gameModel?.apply {
            binding.btn1.text = filledPos[0]
            binding.btn2.text = filledPos[1]
            binding.btn3.text = filledPos[2]
            binding.btn4.text = filledPos[3]
            binding.btn5.text = filledPos[4]
            binding.btn6.text = filledPos[5]
            binding.btn7.text = filledPos[6]
            binding.btn8.text = filledPos[7]
            binding.btn9.text = filledPos[8]

            binding.startGameBtn.visibility = View.VISIBLE

            binding.gameStatusText.text = when (gameStatus) {
                GameStatus.DEFAULT -> {
                    binding.startGameBtn.visibility = View.VISIBLE
                    "X0 2.0"
                }

                GameStatus.CREATED -> {
                    binding.startGameBtn.visibility = View.VISIBLE
                    "Start"
                }

                GameStatus.JOINED -> {
                    binding.startGameBtn.visibility = View.VISIBLE
                    "Click on Start game"
                }

                GameStatus.INPROGRESS -> {
                    binding.startGameBtn.visibility = View.INVISIBLE
                    "$currentTurn's turn"
                }

                GameStatus.FINISHED -> {
                    binding.startGameBtn.visibility = View.VISIBLE
                    if (winner.isNotEmpty()) {
                        "$winner wins"
                    } else {
                        "It's a draw"
                    }
                }
            }
        }
    }

    private fun startGame() {
        gameModel?.let {
            it.filledPos = MutableList(9) { "" }
            it.gameStatus = GameStatus.INPROGRESS
            xQueue.clear()
            oQueue.clear()
            updateGameData(it)
        }
    }

    private fun updateGameData(model: GameModel) {
        GameData.saveGameModel(model)
    }

    private fun Check_win() {
        val WinningPos = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        gameModel?.apply {
            winner = "draw"
            for (i in WinningPos) {
                if ((filledPos[i[0]] == filledPos[i[1]]) &&
                    (filledPos[i[0]] == filledPos[i[2]]) &&
                    filledPos[i[0]].isNotEmpty()
                ) {
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                }
            }

            if (filledPos.none { it.isEmpty() }) {
                gameStatus = GameStatus.FINISHED
            }
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if (gameStatus != GameStatus.INPROGRESS) {
                Toast.makeText(applicationContext, "Game not started", Toast.LENGTH_SHORT).show()
                return
            }

            val clicked = v?.tag?.toString()?.toIntOrNull() ?: return
            if (clicked !in filledPos.indices) return
            if (filledPos[clicked].isNotEmpty()) return

            val queue = if (currentTurn == "X") xQueue else oQueue

            // Remove oldest if queue is full
            if (queue.size >= 3) {
                val indexToRemove = queue.removeFirst()
                filledPos[indexToRemove] = ""
            }

            // Add new move
            queue.addLast(clicked)
            filledPos[clicked] = currentTurn

            // Switch turn
            currentTurn = if (currentTurn == "X") "O" else "X"

            // Check for win
            Check_win()

            // Update UI and game data
            updateGameData(this)
        }
    }
}







