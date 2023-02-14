package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.MessageItemVoiceBinding
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.VoicePlayer
import com.example.clonegram.utils.time

class VoiceMessageHolder(val binding: MessageItemVoiceBinding) :
    RecyclerView.ViewHolder(binding.root), MessageHolder {

    private val voicePlayer = VoicePlayer()

    override fun drawMessage(view: MessageView) {
        if (view.from == UID) {
            with(binding) {
                blocUserVoiceMessage.visibility = View.VISIBLE
                blocReceiverVoiceMessage.visibility = View.GONE
                chatUserBtnPlay.visibility = View.VISIBLE
                chatUserVoiceMessageTime.text =
                    view.timeStamp.time()
            }
        } else {
            with(binding) {
                blocUserVoiceMessage.visibility = View.GONE
                blocReceiverVoiceMessage.visibility = View.VISIBLE
                chatReceiverVoiceMessageTime.text =
                    view.timeStamp.time()
            }
        }
    }

    override fun onAttach(view: MessageView) {
        voicePlayer.init()
        with(binding) {
            if (view.from == UID) {
                chatUserBtnPlay.setOnClickListener {
                    chatUserBtnPlay.visibility = View.GONE
                    chatUserBtnPause.visibility = View.VISIBLE
                    chatUserBtnPause.setOnClickListener {
                        pause{
                            chatUserBtnPause.setOnClickListener (null)
                            chatUserBtnPlay.visibility = View.VISIBLE
                            chatUserBtnPause.visibility = View.GONE
                        }
                    }
                    play(view){
                        chatUserBtnPlay.visibility = View.VISIBLE
                        chatUserBtnPause.visibility = View.GONE
                    }

                }
            } else {
                chatReceiverBtnPlay.setOnClickListener {
                    chatReceiverBtnPlay.visibility = View.GONE
                    chatReceiverBtnPause.visibility = View.VISIBLE
                    chatReceiverBtnPause.setOnClickListener {
                        pause {
                            chatReceiverBtnPause.setOnClickListener (null)
                            chatReceiverBtnPlay.visibility = View.VISIBLE
                            chatReceiverBtnPause.visibility = View.GONE
                        }
                    }
                    play(view) {
                        chatReceiverBtnPlay.visibility = View.VISIBLE
                        chatReceiverBtnPause.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun pause(function: () -> Unit) {
        voicePlayer.pause{
            function()
        }
    }

    private fun play(view: MessageView, function: () -> Unit) {
        voicePlayer.play(view.id, view.fileUrl) {
            function()
        }
    }

    override fun onDetach() {
        binding.chatUserBtnPlay.setOnClickListener(null)
        binding.chatReceiverBtnPlay.setOnClickListener(null)
        voicePlayer.release()
    }
}