package com.example.clonegram.presentation

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.clonegram.databinding.ContactsFragmentBinding

class ContactsFragment : Fragment() {

    private var _binding: ContactsFragmentBinding? = null
    private val binding: ContactsFragmentBinding
        get() = _binding ?: throw RuntimeException("ContactsFragmentBinding is null")

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

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            requestContacts()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
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
            Log.d("contact", "$id $name")

            val phoneCursor = requireActivity().contentResolver.query(
                ContactsContract.CommonDataKinds
                    .Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds
                    .Phone.CONTACT_ID + " = ?",
                arrayOf(id),
                null)
            while (phoneCursor?.moveToNext() == true) {
                val phone = phoneCursor.getString(
                    phoneCursor.getColumnIndexOrThrow(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                )
                Log.d("contact", " $phone")
            }
            phoneCursor?.close()
        }
        cursor?.close()


    }

    companion object {
        private const val READ_CONTACTS_RC = 101
    }
}