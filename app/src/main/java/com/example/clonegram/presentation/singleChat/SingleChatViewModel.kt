package com.example.clonegram.presentation.singleChat

import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.usecases.user.UpdateChildrenUseCase
import com.example.clonegram.utils.*
import com.google.firebase.database.ServerValue
import javax.inject.Inject

class SingleChatViewModel @Inject constructor(
    val updateChildrenUseCase: UpdateChildrenUseCase
) : ViewModel() {

    fun saveToChatList(id: String, type: String) {
        val refUser = "$NODE_SINGLE_CHAT/$UID/$id"
        val refReceiver = "$NODE_SINGLE_CHAT/$id/$UID"

        val mapUser = hashMapOf<String, Any>()
        val mapReceiver = hashMapOf<String, Any>()

        mapUser[CHILD_ID] = id
        mapUser[CHILD_TYPE] = type

        mapReceiver[CHILD_ID] = UID
        mapReceiver[CHILD_TYPE] = type

        val commonMap = hashMapOf<String, Any>()
        commonMap[refUser] = mapUser
        commonMap[refReceiver] = mapReceiver

        updateChildrenUseCase(commonMap) {
        }
    }

    fun sendMessage(
        message: String,
        receivingUserId: String,
        messageType: String,
        function: () -> Unit
    ) {
        val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
        val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"
        val messageKey = REF_DATABASE_ROOT.child(refDialogUser).push().key

        val mapMessage = hashMapOf<String, Any>()
        mapMessage[CHILD_FROM] = UID
        mapMessage[CHILD_TEXT] = message
        mapMessage[CHILD_ID] = messageKey.toString()
        mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
        mapMessage[CHILD_TYPE] = messageType

        val mapDialog = hashMapOf<String, Any>()
        mapDialog["$refDialogUser/$messageKey"] = mapMessage
        mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

        updateChildrenUseCase

        updateChildrenUseCase(mapDialog) {
            function()
        }
    }
}