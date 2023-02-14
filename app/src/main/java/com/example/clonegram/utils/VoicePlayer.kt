package com.example.clonegram.utils

import android.media.MediaPlayer
import java.io.File
import java.lang.Exception

class VoicePlayer {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var file: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        file = File(APP_ACTIVITY.filesDir, messageKey)
        if (file.isFile && file.exists() && file.length() > 0) {
            startPlay {
                function()
            }
        }
    }

    fun pause(function: () -> Unit) {
        try {
            mediaPlayer.stop()
            mediaPlayer.reset()
            function()
        }catch (e:Exception){
            showToast(e.message.toString())
            function()
        }

    }

    fun release() {
        mediaPlayer.release()
    }

    fun init(){
        mediaPlayer = MediaPlayer()
    }

    private fun startPlay(function: () -> Unit) {
        try {
            mediaPlayer.setDataSource(file.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                pause {
                    function()
                }
            }
        }catch (e:Exception){
            showToast(e.message.toString())
        }
    }

}