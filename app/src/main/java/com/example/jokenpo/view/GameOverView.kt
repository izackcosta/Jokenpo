package com.example.jokenpo.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jokenpo.R

@Composable
fun GameOverScreen(modifier: Modifier = Modifier, points : Int, bestScore: Int, retry : () -> Unit, mute : () -> Unit){
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(stringResource(R.string.game_over),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold)
        val pointsText : String = "${stringResource(R.string.points)} $points"
        Text(pointsText)
        val bestScoreText : String = "${stringResource(R.string.best_score)} $bestScore"
        Text(bestScoreText)
        Button(onClick = {retry()}) {
            Text(stringResource(R.string.retry))
        }
        Button(onClick = {mute()} , modifier = Modifier.size(100.dp)) {
            Image(painter = painterResource(R.drawable.audio), stringResource(R.string.audio_description))
        }
    }
}