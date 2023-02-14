package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.MessageItemTextBinding
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.time

class TextMessageHolder(val binding: MessageItemTextBinding) :
    RecyclerView.ViewHolder(binding.root), MessageHolder {

    override fun drawMessage(view: MessageView) {
        if (view.from == UID) {
            with(binding) {
                blocUserMessage.visibility = View.VISIBLE
                blocReceiverMessage.visibility = View.GONE
                chatUserMessage.text = view.text.trim()
                chatUserMessageTime.text =
                    view.timeStamp.time()
            }
        } else {
            with(binding) {
                blocUserMessage.visibility = View.GONE
                blocReceiverMessage.visibility = View.VISIBLE
                chatReceiverMessage.text = view.text.trim()
                chatReceiverMessageTime.text =
                    view.timeStamp.time()
            }
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}