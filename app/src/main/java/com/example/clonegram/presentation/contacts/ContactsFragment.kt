package com.example.clonegram.presentation.contacts

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.clonegram.ClonegramApp
import com.example.clonegram.ViewModelFactory
import com.example.clonegram.databinding.ContactsFragmentBinding
import com.example.clonegram.domain.models.Contact
import com.example.clonegram.presentation.contacts.contactAdapter.ContactsAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ClonegramApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ContactViewModel


    private var _binding: ContactsFragmentBinding? = null
    private val binding: ContactsFragmentBinding
        get() = _binding ?: throw RuntimeException("ContactsFragmentBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLocationPermissionRequest()

        viewModel = ViewModelProvider(this,viewModelFactory)[ContactViewModel::class.java]

        val adapter = ContactsAdapter()
        binding.contactsRecyclerView.adapter = adapter
        viewModel.contactList().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestContacts()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    private fun requestContacts() {

        val cursor = requireActivity().contentResolver.query(
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

            val phoneCursor = requireActivity().contentResolver.query(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val READ_CONTACTS_RC = 101
    }
}