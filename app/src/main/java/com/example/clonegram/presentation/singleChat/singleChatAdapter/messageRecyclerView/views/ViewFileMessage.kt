package com.example.clonegram.presentation.singleChat.singleChatAdapter.messageRecyclerView.views

class ViewFileMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String,
) : MessageView {


    override fun getTypeView(): Int {
       return MessageView.MESSAGE_FILE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }
}