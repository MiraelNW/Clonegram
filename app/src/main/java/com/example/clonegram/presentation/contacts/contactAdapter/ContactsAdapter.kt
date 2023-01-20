package com.example.clonegram.presentation.contacts.contactAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.clonegram.databinding.ContactItemBinding
import com.example.clonegram.domain.models.Contact

class ContactsAdapter : ListAdapter<Contact, ContactsViewHolder>(ContactsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ContactItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = getItem(position)
        Log.d("contact",contact.name)
        Log.d("contact",contact.toString())
        with(holder.binding){
            name.text = contact.name

            phoneNumber.text = contact.number
        }
    }
}