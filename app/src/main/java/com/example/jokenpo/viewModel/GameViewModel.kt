package com.example.jokenpo.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jokenpo.Choice
import com.example.jokenpo.R
import com.example.jokenpo.RoundResult
import com.example.jokenpo.data.GameUiState
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel(){

    private val _gameState = MutableStateFlow(GameUiState())
    val gameState : StateFlow<GameUiState> = _gameState.asStateFlow()

    private val MILISECONDS_TO_SHOW_COMPUTER_CHOICE : Long = 1000
    private val MILISECONDS_TO_SHOW_ROUND_RESULT : Long = 1000
    private val MILISECONDS_TO_NEXT_ROUND : Long = 1000

    fun decrementLives(){
        _gameState.update {
            it.copy(lives = it.lives - 1)
        }
    }

    fun choose(choice : Choice, callGameOVer : (points : Int) -> Unit,
               playWinSound : () -> Unit,
               playLoseSound : () -> Unit){
        if(_gameState.value.waiting) return
        _gameState.update {
            val newMap : MutableMap<Choice, Color> = generateDeselectedButtons()
            newMap[choice] = Color.Green
            it.copy(
                buttonStates = newMap,
                playerChoice = choice,
                waiting = true
            )
        }
        viewModelScope.launch {
            waitBeforeScoringRound(callGameOVer, playWinSound, playLoseSound)
        }
    }

    //Corrotina que espera alguns segundos depois que o jogador escolhe antes de mostrar o resultado da partida
    private suspend fun waitBeforeScoringRound(callGameOVer: (points : Int) -> Unit,
                                               playWinSound: () -> Unit,
                                               playLoseSound: () -> Unit) = withContext(Dispatchers.Default){
        delay(MILISECONDS_TO_SHOW_COMPUTER_CHOICE)
        randomizeEnemyChoice()
        delay(MILISECONDS_TO_SHOW_ROUND_RESULT)
        _gameState.update {
            it.copy(roundResult = checkWinner(it.playerChoice, it.computerChoice))
        }
        delay(MILISECONDS_TO_NEXT_ROUND)
        if(_gameState.value.roundResult == RoundResult.PLAYER_WIN){
            playWinSound()
            _gameState.update {
                it.copy(points = it.points + 1)
            }
        }
        else if(_gameState.value.roundResult == RoundResult.COMPUTER_WIN){
            _gameState.update {
                it.copy(lives = it.lives -1)
            }
            playLoseSound()
            if(_gameState.value.lives <= 0){
                callGameOVer(_gameState.value.points)
                return@withContext
            }
        }
        _gameState.update {
            it.copy(
                roundResult = RoundResult.NONE,
                computerChoice = Choice.NONE,
                buttonStates = generateDeselectedButtons(),
                waiting = false
            )
        }
    }

    private fun randomizeEnemyChoice(){
        val possibleChoices = listOf(Choice.STONE, Choice.PAPER, Choice.SCISORS)
        _gameState.update {
            it.copy(computerChoice = possibleChoices.random())
        }
    }

    private fun checkWinner(playerChoice : Choice, computerChoice: Choice) : RoundResult{

        if(playerChoice == computerChoice) return RoundResult.DRAW
        if((playerChoice == Choice.STONE && computerChoice == Choice.SCISORS)
            || (playerChoice == Choice.PAPER && computerChoice == Choice.STONE)
            || (playerChoice == Choice.SCISORS && computerChoice == Choice.PAPER))
            return RoundResult.PLAYER_WIN
        return RoundResult.COMPUTER_WIN

    }

    private fun generateDeselectedButtons() : MutableMap<Choice, Color>{
        val newMap : MutableMap<Choice, Color> = mutableMapOf(
            Choice.STONE to Color.Red,
            Choice.PAPER to Color.Red,
            Choice.SCISORS to Color.Red
        )
        return newMap
    }

}