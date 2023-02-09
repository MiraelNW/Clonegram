package com.example.clonegram.presentation.singleChat.singleChatAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.R
import com.example.clonegram.databinding.UserMessageItemBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.time

class SingleChatAdapter : RecyclerView.Adapter<SingleChatViewHolder>() {

    private var listMessagesCache = mutableListOf<UserInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder {
        val binding =UserMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChatViewHolder, position: Int) {
        if (listMessagesCache[position].from == UID) {
            with(holder.binding){
                blocUserMessage.visibility = View.VISIBLE
                blocReceiverMessage.visibility = View.GONE
                chatUserMessage.text = listMessagesCache[position].text
                chatUserMessageTime.text =
                    listMessagesCache[position].timeStamp.toString().time()
            }
        } else {
            with(holder.binding){
                blocUserMessage.visibility = View.GONE
                blocReceiverMessage.visibility = View.VISIBLE
                chatReceiverMessage.text = listMessagesCache[position].text
                chatReceiverMessageTime.text =
                    listMessagesCache[position].timeStamp.toString().time()
            }
        }
    }

    override fun getItemCount(): Int = listMessagesCache.size

    override fun onViewRecycled(holder: SingleChatViewHolder) {
        super.onViewRecycled(holder)
    }

    fun addItem(
        item: UserInfo,
        toBottom: Boolean,
        onSuccess: () -> Unit
    ) {
        if (toBottom) {
            if (!listMessagesCache.contains(item)) {
                listMessagesCache.add(item)
                notifyItemInserted(listMessagesCache.size)
            }
        } else {
            if (!listMessagesCache.contains(item)) {
                listMessagesCache.add(item)
                listMessagesCache.sortBy { it.timeStamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSuccess()
    }
}