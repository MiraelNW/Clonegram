package com.example.clonegram.presentation.singleChat

import android.media.MediaRecorder
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.domain.usecases.user.InitReceiverUseCase
import com.example.clonegram.domain.usecases.user.InsertImageUseCase
import com.example.clonegram.domain.usecases.user.UpdateChildrenUseCase
import com.example.clonegram.domain.usecases.user.UploadFileUseCase
import com.example.clonegram.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class SingleChatViewModel @Inject constructor(
    private val voiceRecorder: VoiceRecorder,
    val uploadFileUseCase: UploadFileUseCase,
    val insertImageUseCase: InsertImageUseCase,
    val initReceiverUseCase: InitReceiverUseCase,
    val updateChildrenUseCase: UpdateChildrenUseCase
) : ViewModel() {

    fun startRecord(userId: String) {
        val messageKey =
            REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(userId)
                .push().key.toString()
        voiceRecorder.startRecord(messageKey)
    }

    fun stopRecord(userId: String) {
        voiceRecorder.stopRecord() { file, messageKey ->
            uploadFileUseCase(
                Uri.fromFile(file),
                messageKey,
                userId,
                TYPE_VOICE
            )
        }
    }

    fun releaseRecorder() {
        voiceRecorder.releaseRecorder()
    }

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