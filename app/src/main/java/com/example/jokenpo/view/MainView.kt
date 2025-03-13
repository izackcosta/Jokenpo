package com.example.jokenpo.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jokenpo.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

//Pela falta de funcionalidade e dados nessa tela, ela usará apenas o compose sem uma viewModel
@Composable
fun TitleScreen(modifier: Modifier = Modifier, startGame : () -> Unit = {}, mute : () -> Unit = {}){
    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(modifier = Modifier,
            text = stringResource(R.string.title_app_name),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(36.dp))
        Button(modifier = Modifier, onClick = startGame) {
            Text(stringResource(R.string.start_game), modifier = Modifier)
        }
        Button(onClick = {mute()}, modifier = Modifier.size(100.dp)) {
            Image(painter = painterResource(R.drawable.audio), stringResource(R.string.audio_description))
        }
    }
}