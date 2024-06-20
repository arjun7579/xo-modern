package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictactoeonline.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.offlineBtn.setOnClickListener {
            offGame()
            startGame()
        }

    }

    fun offGame() {

        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOINED
            )
        )

    }

    fun startGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }
}