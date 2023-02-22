package com.example.clonegram.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clonegram.ClonegramApp
import com.example.clonegram.databinding.ActivityMainBinding
import com.example.clonegram.utils.*

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        (application as ClonegramApp).component
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        APP_ACTIVITY = this
        initFirebaseDatabase()
    }

    override fun onStart() {
        super.onStart()
        UserState.updateState(UserState.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        UserState.updateState(UserState.OFFLINE)
    }
}