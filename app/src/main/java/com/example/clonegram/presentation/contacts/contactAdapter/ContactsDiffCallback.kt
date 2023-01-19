package com.example.clonegram.presentation.contacts.contactAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.clonegram.domain.models.Contact

object ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}