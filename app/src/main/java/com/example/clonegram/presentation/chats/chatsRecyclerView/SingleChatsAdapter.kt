package com.example.clonegram.presentation.chats.chatsRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.clonegram.R
import com.example.clonegram.databinding.SingleChatItemBinding
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.utils.UID
import com.example.clonegram.utils.downloadAndSetImage
import com.example.clonegram.utils.time
import com.squareup.picasso.Picasso

class SingleChatsAdapter :
    ListAdapter<UserInfo, SingleChatsViewHolder>(SingleChatsDiffCallback) {

    var onChatClickListener: OnChatClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatsViewHolder {
        val binding =
            SingleChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChatsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleChatsViewHolder, position: Int) {
        val user = getItem(position)
        with(holder.binding) {
            if (user.id == UID) {
                singleChatName.text = "Favourites"
                singleChatLastMessage.text = user.lastMessage
                singleChatItem.setBackgroundResource(R.color.colorPrimary)
                Picasso.get().load(R.drawable.ic_favourite).fit().into(singleChatUserAvatar)
                singleChatLastMessageTime.text = user.timeStamp.toString().time()
            } else {
                singleChatName.text = user.name
                singleChatLastMessage.text = user.lastMessage
                singleChatUserAvatar.downloadAndSetImage(user.photoUrl)
                singleChatLastMessageTime.text = user.timeStamp.toString().time()
            }
        }
        holder.itemView.setOnClickListener {
            onChatClickListener?.onChatClick(user)
        }

    }

    interface OnChatClickListener {
        fun onChatClick(user: UserInfo)
    }

}