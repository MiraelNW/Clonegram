package com.example.clonegram.presentation.chats.chatsRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.clonegram.domain.models.UserInfo

object SingleChatsDiffCallback  : DiffUtil.ItemCallback<UserInfo>(){
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
       return oldItem.lastMessage == newItem.lastMessage
    }

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean {
       return oldItem == newItem
    }
}