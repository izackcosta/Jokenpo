package com.example.jokenpo.view

import android.media.SoundPool
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jokenpo.Choice
import com.example.jokenpo.R
import com.example.jokenpo.RoundResult
import com.example.jokenpo.viewModel.GameViewModel

@Composable
fun GameScreen(modifier : Modifier = Modifier, callGameOver : (points : Int) -> Unit = {},
               playCursorSound : () -> Unit = {},
               playWinSound : () -> Unit = {},
               playLoseSound : () -> Unit = {},
               mute : () -> Unit = {},
               viewModel: GameViewModel = viewModel()){
    Column (modifier = Modifier.fillMaxSize()
        .padding(top = 30.dp)){
        LivesDisplay(viewModel = viewModel)
        PlayArea(modifier = Modifier,
            callGameOver,
            playCursorSound,
            playWinSound,
            playLoseSound,
            viewModel = viewModel)
        ResultText(viewModel)
        Spacer(modifier = Modifier.size(50.dp))
        val pointsText = stringResource(R.string.points)
        val points = viewModel.gameState.collectAsState().value.points
        Text("$pointsText $points", modifier = Modifier,
            fontSize = 24.sp)
        Button(onClick = {mute()} , modifier = Modifier.size(100.dp)) {
            Image(painter = painterResource(R.drawable.audio), stringResource(R.string.audio_description))
        }
    }
}

@Composable
fun LivesDisplay(modifier: Modifier = Modifier, viewModel: GameViewModel){
    Row (modifier = Modifier.fillMaxWidth()
        .height(50.dp)){
        Text(stringResource(R.string.lives),
            modifier = Modifier.width(75.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically),
            fontSize = 24.sp
            )
        for(i in 1 .. viewModel.gameState.collectAsState().value.lives){
            Image(painter = painterResource(R.drawable.heart),
                contentDescription = stringResource(R.string.heart_content_description)
            )
        }
    }
}

@Composable
fun PlayArea(modifier: Modifier = Modifier, callGameOver: (points : Int) -> Unit,
             playCursorSound : () -> Unit,
             playWinSound : () -> Unit,
             playLoseSound: () -> Unit,
             viewModel: GameViewModel){
    Row (modifier = Modifier.padding(top = 25.dp),
        verticalAlignment = Alignment.CenterVertically){
        Column (modifier = Modifier){
            JokenpoButton(choice = Choice.STONE, callGameOver, playCursorSound, playWinSound, playLoseSound, viewModel)
            JokenpoButton(choice = Choice.PAPER, callGameOver, playCursorSound, playWinSound, playLoseSound, viewModel)
            JokenpoButton(choice = Choice.SCISORS, callGameOver, playCursorSound, playWinSound, playLoseSound, viewModel)
        }
        Column (modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                stringResource(R.string.enemy_choice), modifier = Modifier.fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
                fontSize = 24.sp)
            EnemyChoiceDisplay(viewModel.gameState.collectAsState().value.computerChoice)
        }
    }
}

@Composable
fun JokenpoButton(choice: Choice,callGameOver: (points : Int) -> Unit,
                  playCursorSound : () -> Unit,
                  playWinSound : () -> Unit,
                  playLoseSound: () -> Unit,
                  viewModel: GameViewModel){
    Button(onClick = {
        playCursorSound()
        viewModel.choose(choice, callGameOver, playWinSound, playLoseSound)
                     },
        modifier = Modifier.size(150.dp)
            .padding(top = 25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = viewModel.gameState
            .collectAsState().value.buttonStates[choice]!!
        )) {
        val painter : Painter = getPaintFromChoice(choice)
        val description : String = getDescriptionFromChoice(choice)
        Image(painter, description)
    }
}

@Composable
fun EnemyChoiceDisplay(choice : Choice, modifier: Modifier = Modifier){
    val painter : Painter = getPaintFromChoice(choice)
    val description : String = getDescriptionFromChoice(choice)
    Image(painter, description, modifier = Modifier.size(150.dp))
}

@Composable
fun ResultText(viewModel: GameViewModel){
    val text : String = when(viewModel.gameState.collectAsState().value.roundResult){
        RoundResult.NONE -> ""
        RoundResult.PLAYER_WIN -> stringResource(R.string.win_result)
        RoundResult.COMPUTER_WIN -> stringResource(R.string.lose_result)
        RoundResult.DRAW -> stringResource(R.string.draw_result)
    }
    val color : Color = when(viewModel.gameState.collectAsState().value.roundResult){
        RoundResult.NONE -> Color.White
        RoundResult.PLAYER_WIN -> Color.Green
        RoundResult.COMPUTER_WIN -> Color.Red
        RoundResult.DRAW -> Color.Blue
    }
    Spacer(modifier = Modifier.size(50.dp))
    Text(text, color = color, modifier = Modifier.fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally),
        fontSize = 24.sp)
}

@Composable
fun getPaintFromChoice(choice : Choice) : Painter {
    val painter: Painter = when (choice) {
        Choice.STONE -> painterResource(R.drawable.stone)
        Choice.PAPER -> painterResource(R.drawable.paper)
        Choice.SCISORS -> painterResource(R.drawable.scisors)
        Choice.NONE -> painterResource(R.drawable.interrogation)
    }
    return painter
}

@Composable
fun getDescriptionFromChoice(choice : Choice) : String{
    val description : String = when(choice){
        Choice.STONE -> stringResource(R.string.stone_content_description)
        Choice.PAPER -> stringResource(R.string.paper_content_description)
        Choice.SCISORS -> stringResource(R.string.scisors_content_description)
        Choice.NONE -> stringResource(R.string.none_content_description)
    }
    return description
}

@Preview
@Composable
fun Preview(){
    GameScreen()
}