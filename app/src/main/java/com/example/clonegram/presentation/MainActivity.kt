package com.example.clonegram.presentation

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.ActivityMainBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.authentication.EnterCodeFragment
import com.example.clonegram.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

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