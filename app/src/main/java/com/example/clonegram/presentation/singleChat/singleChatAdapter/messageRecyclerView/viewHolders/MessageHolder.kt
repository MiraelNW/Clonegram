package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView

interface MessageHolder {
    fun drawMessage(view:MessageView)
    fun onAttach(view : MessageView)
    fun onDetach()
}