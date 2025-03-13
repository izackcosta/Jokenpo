package com.example.jokenpo.data

import androidx.compose.ui.graphics.Color
import com.example.jokenpo.Choice
import com.example.jokenpo.RoundResult

data class GameUiState(
    val lives : Int = 3,
    val buttonStates: MutableMap<Choice, Color> = mutableMapOf(
        Choice.STONE to Color.Red,
        Choice.PAPER to Color.Red,
        Choice.SCISORS to Color.Red
    ),
    val waiting : Boolean = false,
    val computerChoice : Choice = Choice.NONE,
    val playerChoice : Choice = Choice.NONE,
    val roundResult: RoundResult = RoundResult.NONE,
    val points : Int = 0
)
