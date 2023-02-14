package com.example.clonegram.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.MainActivity
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var APP_ACTIVITY: MainActivity

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var UID: String
lateinit var USER: UserInfo

const val NODE_USERS = "users"
const val NODE_USERS_ID = "usersid"
const val NODE_PHONES_NUMBER = "phones"
const val NODE_PHONES_CONTACT = "phone_contact"
const val NODE_MESSAGES = "messages"

const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_IMAGE_MESSAGE = "image_message"
const val FOLDER_FILE_MESSAGE = "file_message"

const val TYPE_TEXT = "text"
const val TYPE_IMAGE = "image"
const val TYPE_VOICE = "voice"

const val CHILD_ID = "id"
const val CHILD_ID_NAME = "idName"
const val CHILD_PHONE = "phone"
const val CHILD_NAME = "name"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO = "photoUrl"
const val CHILD_FILE = "fileUrl"
const val CHILD_STATE = "state"
const val CHILD_TEXT = "text"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_TIMESTAMP = "timeStamp"

fun initFirebaseDatabase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    UID = AUTH.currentUser?.uid.toString()
    USER = UserInfo()
}

fun putUrlToDatabase(url: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .child(CHILD_PHOTO).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun getUrlFromStorage(path: StorageReference, function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun putFileToStorage(uri: Uri, path: StorageReference, function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

private fun sendFileMessage(receivingUserId: String, fileUrl: String, messageKey: String, typeMessage: String) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_FILE] = fileUrl

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}


fun uploadFileToStorage(uri: Uri, messageKey: String, receivedId: String,typeMessage:String) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILE_MESSAGE).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendFileMessage(receivedId, it, messageKey,typeMessage)
        }
    }
}
