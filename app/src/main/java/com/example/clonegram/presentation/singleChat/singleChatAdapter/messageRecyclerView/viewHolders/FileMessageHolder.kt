package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.viewHolders

import android.os.Environment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.clonegram.databinding.MessageItemFileBinding
import com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views.MessageView
import com.example.clonegram.utils.*
import java.io.File

class FileMessageHolder(val binding: MessageItemFileBinding) :
    RecyclerView.ViewHolder(binding.root), MessageHolder {

    override fun drawMessage(view: MessageView) {
        if (view.from == UID) {
            with(binding) {
                blocUserFileMessage.visibility = View.VISIBLE
                blocReceiverFileMessage.visibility = View.GONE
                chatUserFileMessageTime.text =
                    view.timeStamp.time()
                chatUserFilename.text = view.text
            }
        } else {
            with(binding) {
                blocUserFileMessage.visibility = View.GONE
                blocReceiverFileMessage.visibility = View.VISIBLE
                chatReceiverFileMessageTime.text =
                    view.timeStamp.time()
                chatReceiverFilename.text = view.text
            }
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == UID) {
            binding.chatUserBtnDownload.setOnClickListener { downloadFile(view) }
        } else {
            binding.chatReceiverBtnDownload.setOnClickListener { downloadFile(view) }
        }
    }

    private fun downloadFile(view: MessageView) {
        if (view.from == UID) {
            binding.chatUserBtnDownload.visibility = View.INVISIBLE
            binding.chatUserBtnDownload.isClickable = false
            binding.chatUserProgressBar.visibility = View.VISIBLE

        } else {
            binding.chatReceiverBtnDownload.visibility = View.INVISIBLE
            binding.chatReceiverBtnDownload.isClickable = false
            binding.chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            file.createNewFile()
            getFileFromStorage(file, view.fileUrl) {
                if (view.from == UID) {
                    binding.chatUserBtnDownload.visibility = View.VISIBLE
                    binding.chatUserBtnDownload.isClickable = true
                    binding.chatUserProgressBar.visibility = View.INVISIBLE

                } else {
                    binding.chatReceiverBtnDownload.visibility = View.VISIBLE
                    binding.chatReceiverBtnDownload.isClickable = true
                    binding.chatReceivedProgressBar.visibility = View.INVISIBLE
                }
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    override fun onDetach() {
        binding.chatUserBtnDownload.setOnClickListener(null)
        binding.chatReceiverBtnDownload.setOnClickListener(null)
    }


}