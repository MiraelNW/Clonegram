package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views

import android.util.Log
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.TYPE_FILE
import com.example.clonegram.utils.TYPE_IMAGE
import com.example.clonegram.utils.TYPE_TEXT
import com.example.clonegram.utils.TYPE_VOICE

class ViewFactory {
    companion object {
        fun getView(message: UserInfo): MessageView {
            return when (message.type) {
                TYPE_IMAGE -> {
                    ViewImageMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl
                    )
                }
                TYPE_VOICE -> {
                    ViewVoiceMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl
                    )
                }
                TYPE_FILE -> {
                    ViewFileMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl,
                        message.text
                    )
                }
                else ->{
                    ViewTextMessage(
                        message.id,
                        message.from,
                        message.timeStamp.toString(),
                        message.fileUrl,
                        message.text
                    )
                }
            }
        }
    }
}