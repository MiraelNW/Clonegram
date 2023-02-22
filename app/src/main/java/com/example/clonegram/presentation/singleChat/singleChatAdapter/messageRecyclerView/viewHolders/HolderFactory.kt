package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.MessageItemFileBinding
import com.example.clonegram.databinding.MessageItemImageBinding
import com.example.clonegram.databinding.MessageItemTextBinding
import com.example.clonegram.databinding.MessageItemVoiceBinding
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView

class HolderFactory {
    companion object{
        fun getHolder(parent: ViewGroup,viewType:Int):RecyclerView.ViewHolder{
            return when(viewType){
                MessageView.MESSAGE_IMAGE ->  {
                    val binding =
                        MessageItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    ImageMessageHolder(binding)
                }
                MessageView.MESSAGE_TEXT ->  {
                    val binding =
                        MessageItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    TextMessageHolder(binding)
                }
                MessageView.MESSAGE_VOICE ->  {
                    val binding =
                        MessageItemVoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    VoiceMessageHolder(binding)
                }
                MessageView.MESSAGE_FILE ->  {
                    val binding =
                        MessageItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    FileMessageHolder(binding)
                }
                else ->{
                    val binding =
                        MessageItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    TextMessageHolder(binding)
                }

            }
        }
    }
}