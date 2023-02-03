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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope

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

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        startLocationPermissionRequest()
        if (AUTH.currentUser == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StartCommunicationFragment.newInstance())
                .commit()
        } else {
            initUser() {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ChatsFragment.newInstance())
                    .commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        UserState.updateState(UserState.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        UserState.updateState(UserState.OFFLINE)
    }

    private fun initUser(function: () -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                    function()
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
            showToast("Permission denied")
            finish()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    private fun requestContacts() {
        val arrayContacts = arrayListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val name = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val id = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                var phone = cursor
                    .getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val number = phone.replace(Regex("-"), "").replace(Regex(" "), "")
                phone = number.substring(0, 2) + " (" +
                        number.substring(2, 5) + ") " +
                        number.substring(5, 8) + " " +
                        number.substring(8, 10) + " " +
                        number.substring(10, 12)
                val contact = Contact(
                    id = id,
                    name = name,
                    phone = phone
                )
                arrayContacts.add(contact)
            }
        }
        updatePhonesList(arrayContacts)
        cursor?.close()
    }

    private fun updatePhonesList(arrayContacts: ArrayList<Contact>) {
        REF_DATABASE_ROOT.child(NODE_PHONES_NUMBER)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { snapshot ->
                        arrayContacts.forEach { contact ->
                            Log.d("phoro", contact.toString())
                            Log.d("phoro", snapshot.key.toString())
                            if (snapshot.key == contact.phone) {

                                insertPhoto(snapshot){
                                    CoroutineScope(Dispatchers.IO).launch {

                                        viewModel.insertContact.invoke(it)
                                    }
                                }


                                REF_DATABASE_ROOT.child(NODE_PHONES_CONTACT).child(UID)
                                    .child(snapshot.value.toString()).child(CHILD_ID)
                                    .setValue(snapshot.value.toString())
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun insertPhoto(snapshot : DataSnapshot, function: (contact:Contact) -> Unit) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(snapshot.value.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot2: DataSnapshot) {
                   val contact = snapshot2.getValue(Contact::class.java) ?: Contact()

                    function(contact)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


}