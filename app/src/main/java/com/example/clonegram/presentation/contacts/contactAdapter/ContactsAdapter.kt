package com.example.clonegram.presentation.contacts.contactAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.clonegram.databinding.ContactItemBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.downloadAndSetImage

class ContactsAdapter : ListAdapter<UserInfo, ContactsViewHolder>(ContactsDiffCallback) {

    var onContactClickListener : OnContactClickListener? = null

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
        with(holder.binding){
            name.text = contact.name
            phoneNumber.text = contact.phone
            contactAvatar.downloadAndSetImage(contact.photoUrl)
        }
        holder.itemView.setOnClickListener {
            onContactClickListener?.onContactClick(contact)
        }
    }

    interface OnContactClickListener{
        fun onContactClick(contact: UserInfo)
    }
}