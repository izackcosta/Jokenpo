package com.example.jokenpo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jokenpo.ui.theme.JokenpoTheme
import com.example.jokenpo.view.GameOverScreen

class GameOverActivity : ComponentActivity() {

    var points : Int = 0
    var bestScore : Int = 0

    lateinit var mediaPlayer : MediaPlayer

    private val VOLUME : Float = 0.5f
    private var muteState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mediaPlayer = MediaPlayer.create(this, R.raw.game_over)
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayer.start()

        points = intent.getIntExtra("points", 0)
        val sharedPreferences : SharedPreferences = getSharedPreferences(getString(R.string.jokenpo_score), Context.MODE_PRIVATE)
        bestScore = sharedPreferences.getInt(getString(R.string.best_score_key), 0)

        setContent {
            JokenpoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameOverScreen(modifier = Modifier.padding(innerPadding), points, bestScore, {retry()}, {mute()})
                }
            }
        }

    }

    private fun retry(){
        mediaPlayer.stop()
        mediaPlayer.release()
        val titleIntent : Intent = Intent(this, MainActivity::class.java)
        startActivity(titleIntent)
    }

    private fun mute(){
        muteState = !muteState
        if(muteState) mediaPlayer.setVolume(0f, 0f)
        else mediaPlayer.setVolume(VOLUME, VOLUME)
    }

}