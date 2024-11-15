package com.example.tictactoetest

import androidx.lifecycle.ViewModel
import com.example.tictactoetest.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface TicTacToeState {
    data class InProgress(val board: List<Player?>, val currentPlayer: Player) : TicTacToeState
    data class Win(val winner: Player) : TicTacToeState
    data object Draw : TicTacToeState
}

class GameViewModel : ViewModel() {

    private val gameState = GameManager()

    private val _state = MutableStateFlow<TicTacToeState>(
        TicTacToeState.InProgress(
            gameState.board,
            gameState.currentPlayer
        )
    )
    val state: StateFlow<TicTacToeState> = _state

    init {
        startGame()
    }

    private fun startGame() {

        _state.value = TicTacToeState.InProgress(
            board = gameState.board,
            currentPlayer = gameState.currentPlayer
        )
    }

    fun playTurn(position: Int) {
        if (gameState.playTurn(position)) {
            _state.value = when {
                gameState.checkWinner() != null -> TicTacToeState.Win(gameState.checkWinner()!!)
                gameState.checkDraw() -> TicTacToeState.Draw
                else -> TicTacToeState.InProgress(
                    board = gameState.board,
                    currentPlayer = gameState.currentPlayer
                )
            }
        }
    }

    fun resetGame() {
        gameState.resetGame()
        startGame()
    }
}