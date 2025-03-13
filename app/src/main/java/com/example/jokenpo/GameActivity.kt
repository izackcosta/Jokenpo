package com.example.jokenpo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.jokenpo.ui.theme.JokenpoTheme
import com.example.jokenpo.view.GameScreen

class GameActivity : ComponentActivity() {

    lateinit var soundPool : SoundPool
    lateinit var audioAttributes : AudioAttributes
    val MAX_STREAMS = 10

    var cursorSound : Int = 0
    var loseSound : Int = 0
    var winSound : Int = 0

    lateinit var mediaPlayer : MediaPlayer

    val VOLUME : Float = 0.25f
    var muteState : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()


        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(MAX_STREAMS)
            .build()

        cursorSound = soundPool.load(this, R.raw.cursor, 1)
        loseSound = soundPool.load(this, R.raw.lose, 1)
        winSound = soundPool.load(this, R.raw.win, 1)

        mediaPlayer = MediaPlayer.create(this, R.raw.game)
        mediaPlayer.setVolume(0.25f, 0.25f)
        mediaPlayer.start()

        setContent {
            JokenpoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(modifier = Modifier.padding(innerPadding),
                        { points : Int -> callGameOver(points)},
                        {playCursorSound()},
                        {playWinSound()},
                        {playLoseSound()},
                        {mute()})
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun callGameOver(points : Int){
        mediaPlayer.stop()
        mediaPlayer.release()
        val gameOverIntent : Intent = Intent(this, GameOverActivity::class.java)
        gameOverIntent.putExtra("points", points)
        val sharedPreferences : SharedPreferences = getSharedPreferences(getString(R.string.jokenpo_score), Context.MODE_PRIVATE)
        val bestScore : Int = sharedPreferences.getInt(getString(R.string.best_score_key), 0)
        if(points > bestScore){
            val editor : SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt(getString(R.string.best_score_key), points)
            editor.commit()
        }
        startActivity(gameOverIntent)
    }

    private fun playCursorSound(){
        soundPool.play(cursorSound, 1f, 1f, 1, 0, 1f)
    }

    private fun playLoseSound(){
        soundPool.play(loseSound, 1f, 1f, 1, 0, 1f)
    }

    private fun playWinSound(){
        soundPool.play(winSound, 1f, 1f, 1, 0, 1f)
    }

    private fun mute(){
        muteState = !muteState
        if(muteState) mediaPlayer.setVolume(0f, 0f)
        else mediaPlayer.setVolume(VOLUME, VOLUME)
    }

}