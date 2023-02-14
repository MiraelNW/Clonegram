package com.example.clonegram.utils

import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import com.example.clonegram.presentation.MainActivity
import java.io.File
import java.lang.Exception

class VoiceRecorder {

    private val mediaRecorder = MediaRecorder()
    private lateinit var file: File
    private lateinit var mesKey: String

    fun startRecord(messageKey: String) {
        try {
            mesKey = messageKey
            createFileForRecorder()
            prepareMediaRecorder()
            mediaRecorder.start()
        } catch (e: Exception) {
            file.delete()
           Toast.makeText(APP_ACTIVITY,e.message.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    fun stopRecord(onSuccess: (file:File,messageKey:String) -> Unit) {
        try {
            mediaRecorder.stop()
            onSuccess(file,mesKey)
        } catch (e: Exception) {
            file.delete()
            Toast.makeText(APP_ACTIVITY,"${e.message.toString()} +++++",Toast.LENGTH_SHORT).show()
        }

    }

    fun releaseRecorder() {
        try {
            mediaRecorder.release()
        }catch (e:Exception){
            Toast.makeText(APP_ACTIVITY,"${e.message.toString()} ++",Toast.LENGTH_SHORT).show()
        }
    }

    private fun createFileForRecorder() {
        file = File(APP_ACTIVITY.filesDir, mesKey)
        file.createNewFile()
    }

    private fun prepareMediaRecorder() {
        mediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(file.absolutePath)
            prepare()
        }

    }
}