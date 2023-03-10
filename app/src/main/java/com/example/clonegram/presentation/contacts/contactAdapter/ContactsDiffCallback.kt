package com.example.clonegram.presentation.contacts.contactAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.clonegram.domain.models.UserInfo

object ContactsDiffCallback : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
        return oldItem == newItem
    }
}