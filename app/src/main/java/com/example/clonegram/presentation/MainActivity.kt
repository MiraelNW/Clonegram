package com.example.clonegram.presentation

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.ActivityMainBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.presentation.authication.StartCommunicationFragment
import com.example.clonegram.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.clonegram.domain.models.UserInfo

class MainActivity : AppCompatActivity() {

    private val component by lazy {
        (application as ClonegramApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initFirebaseDatabase()
        initUser()
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        startLocationPermissionRequest()
        if (AUTH.currentUser == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StartCommunicationFragment.newInstance())
                .commit()
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,ChatsFragment.newInstance())
                .commit()
        }
    }

    private fun initUser(){
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestContacts()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    private fun requestContacts() {

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
            val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))

            val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds
                    .Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds
                    .Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null
            )

            while (phoneCursor?.moveToNext() == true) {
                val phone = phoneCursor.getString(
                    phoneCursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                )
                val contact = Contact(id, name, phone)
                lifecycleScope.launch {
                    viewModel.insertContact.invoke(contact)
                }
                Log.d("contact", contact.toString())
            }
            phoneCursor?.close()
        }
        cursor?.close()
    }


}