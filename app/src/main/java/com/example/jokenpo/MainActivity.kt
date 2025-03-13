package com.example.jokenpo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jokenpo.ui.theme.JokenpoTheme
import com.example.jokenpo.view.TitleScreen

class MainActivity : ComponentActivity() {

    lateinit var mediaPlayer : MediaPlayer

    private val VOLUME : Float = 0.5f
    private var muteState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mediaPlayer = MediaPlayer.create(this, R.raw.title)
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayer.start()

        setContent {
            JokenpoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TitleScreen(modifier = Modifier.padding(innerPadding), startGame = {startGame()}, {mute()})
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun startGame(){
        mediaPlayer.stop()
        mediaPlayer.release()
        val gameIntent : Intent = Intent(this, GameActivity::class.java)
        startActivity(gameIntent)
    }

    private fun mute(){
        muteState = !muteState
        if(muteState) mediaPlayer.setVolume(0f, 0f)
        else mediaPlayer.setVolume(VOLUME, VOLUME)
    }

}

@Preview
@Composable
fun Preview(){
    JokenpoTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TitleScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}