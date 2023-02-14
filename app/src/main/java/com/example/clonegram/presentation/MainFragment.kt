package com.example.clonegram.presentation

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.clonegram.ClonegramApp
import com.example.clonegram.R
import com.example.clonegram.databinding.MainFragmentBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding
        get() = _binding ?: throw RuntimeException("MainFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        if (AUTH.currentUser == null) {
            findNavController().navigate(R.id.action_mainFragment2_to_startCommunicationFragment2)
        } else {
            initUser() {
                startLocationPermissionRequest()
                findNavController().navigate(R.id.action_mainFragment2_to_chatsFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            requireActivity().finish()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    private fun requestContacts() {
        val arrayContacts = arrayListOf<Contact>()
        val cursor = requireActivity().contentResolver.query(
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
                if (phone != USER.phone) {
                    Log.d("phone",phone)
                    Log.d("phone",USER.phone)
                    val contact = Contact(
                        id = id,
                        name = name,
                        phone = phone
                    )

                    arrayContacts.add(contact)
                }
            }
        }
        updatePhonesList(arrayContacts)
        cursor?.close()
    }

    private fun updatePhonesList(arrayContacts: ArrayList<Contact>) {
        if (AUTH.currentUser != null) {
            REF_DATABASE_ROOT.child(NODE_PHONES_NUMBER)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach { snapshot ->
                            arrayContacts.forEach { contact ->
                                if (snapshot.key == contact.phone) {
                                    insertContact(snapshot) {
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
    }

    private fun insertContact(snapshot: DataSnapshot, function: (contact: Contact) -> Unit) {
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

}