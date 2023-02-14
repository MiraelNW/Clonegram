package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.MessageItemImageBinding
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.downloadAndSetImage
import com.example.clonegram.utils.time

class ImageMessageHolder(val binding: MessageItemImageBinding) :
    RecyclerView.ViewHolder(binding.root), MessageHolder {

    override fun drawMessage(view: MessageView) {
        if (view.from == UID) {
            with(binding) {
                blocUserImageMessage.visibility =View.VISIBLE
                blocReceiverImageMessage.visibility =View.GONE
                chatUserImageMessage
                    .downloadAndSetImage(view.fileUrl)
                chatUserImageMessageTime.text =
                    view.timeStamp.time()
            }
        } else {
            with(binding) {
                blocUserImageMessage.visibility =View.GONE
                blocReceiverImageMessage.visibility = View.VISIBLE
                chatReceiverImageMessage
                    .downloadAndSetImage(view.toString())
                chatReceiverImageMessageTime.text =
                    view.timeStamp.time()
            }
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }

}