package com.example.clonegram.utils

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clonegram.domain.models.UserInfo
import com.example.clonegram.presentation.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.io.File

lateinit var APP_ACTIVITY: MainActivity
private var chatsList = MutableLiveData<List<UserInfo>>()
private var urlFromStorage = MutableLiveData<String>()

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var UID: String
lateinit var USER: UserInfo

lateinit var refChatsList: DatabaseReference
lateinit var refUser: DatabaseReference
lateinit var refMessages: DatabaseReference

const val NODE_USERS = "users"
const val NODE_USERS_ID = "usersid"
const val NODE_PHONES_NUMBER = "phones"
const val NODE_PHONES_CONTACT = "phone_contact"
const val NODE_MESSAGES = "messages"
const val NODE_SINGLE_CHAT = "single_chat"

const val FOLDER_PROFILE_IMAGE = "profile_image"
const val FOLDER_FILE_MESSAGE = "file_message"

const val TYPE_TEXT = "text"
const val TYPE_IMAGE = "image"
const val TYPE_VOICE = "voice"
const val TYPE_FILE = "file"

const val TYPE_SINGLE_CHAT = "single_chat"

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

fun changeFirebaseBio(bio: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(bio)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                function()
            }
        }
}

fun changeUserName(name: String, function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_NAME).setValue(name)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                function()
            }
        }
}

fun getImageUrl(uri: Uri): LiveData<String> {

    val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE).child(UID)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            putUrlToDatabase(it) {
                urlFromStorage.value = it
            }
        }
    }
    return urlFromStorage
}

fun initFirebaseUser(function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                USER = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                function()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
}

fun updateFirebaseChildren(commonMap: HashMap<String, Any>,function: () -> Unit) {
    REF_DATABASE_ROOT.updateChildren(commonMap)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun firstUploadUser(
    phoneNumber: String,
    uid: String,
    dateMap: Map<String, Any>,
    function: () -> Unit
) {
    REF_DATABASE_ROOT.child(NODE_PHONES_NUMBER).child(phoneNumber).setValue(uid)
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                .addOnSuccessListener {
                    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                USER = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                                if (USER.idName.isEmpty()) {
                                    USER.idName = USER.id
                                    REF_DATABASE_ROOT.child(NODE_USERS_ID).child(USER.id)
                                        .setValue(USER.id)
                                }
                                function()
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                }
                .addOnFailureListener { showToast(it.message.toString()) }
        }
        .addOnFailureListener { showToast(it.message.toString()) }
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

fun initFirebaseReceiver(userId:String,function: (UserInfo) -> Unit) {
    getReceiverId(userId) {
        getReceiver(userId) { snapshot ->
            val receiver = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
            function(receiver)
        }
    }
}

private fun getReceiver(userId:String,function: (snapshot: DataSnapshot) -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                function(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
}

private fun getReceiverId(userId:String,function: (snapshot: DataSnapshot) -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS_ID).child(userId)
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                function(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
}

private fun sendFileMessage(
    receivingUserId: String,
    fileUrl: String,
    messageKey: String,
    typeMessage: String,
    filename: String
) {
    val refDialogUser = "$NODE_MESSAGES/$UID/$receivingUserId"
    val refDialogReceivingUser = "$NODE_MESSAGES/$receivingUserId/$UID"

    val mapMessage = hashMapOf<String, Any>()
    mapMessage[CHILD_FROM] = UID
    mapMessage[CHILD_ID] = messageKey
    mapMessage[CHILD_TIMESTAMP] = ServerValue.TIMESTAMP
    mapMessage[CHILD_TYPE] = typeMessage
    mapMessage[CHILD_FILE] = fileUrl
    mapMessage[CHILD_TEXT] = filename

    val mapDialog = hashMapOf<String, Any>()
    mapDialog["$refDialogUser/$messageKey"] = mapMessage
    mapDialog["$refDialogReceivingUser/$messageKey"] = mapMessage

    REF_DATABASE_ROOT
        .updateChildren(mapDialog)
        .addOnFailureListener { showToast(it.message.toString()) }
}


fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedId: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILE_MESSAGE).child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) {
            sendFileMessage(receivedId, it, messageKey, typeMessage, filename)
        }
    }
}

fun getFileFromStorage(file: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(file)
        .addOnSuccessListener { function() }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}


fun getChatsList(): LiveData<List<UserInfo>> {
    refChatsList = REF_DATABASE_ROOT.child(NODE_SINGLE_CHAT).child(UID)
    refUser = REF_DATABASE_ROOT.child(NODE_USERS)
    refMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID)

    getAllChats() { userList ->
        userList.sortByDescending { it.timeStamp.toString() }
        chatsList.value = userList.toList()
    }
    return chatsList
}

fun getAllChats(function: (userList: MutableList<UserInfo>) -> Unit) {

    val userList = mutableListOf<UserInfo>()

    refChatsList.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val listUsers =
                dataSnapshot.children.map { it.getValue(UserInfo::class.java) ?: UserInfo() }
            listUsers.forEach { model ->
                refUser.child(model.id)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val newUser = snapshot.getValue(UserInfo::class.java) ?: UserInfo()
                            refMessages.child(model.id).limitToLast(1)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(messageSnapshot: DataSnapshot) {
                                        val tempList = messageSnapshot.children.map {
                                            it.getValue(UserInfo::class.java) ?: UserInfo()
                                        }
                                        var isNeedToAdd = true
                                        when (tempList[0].type) {
                                            TYPE_VOICE -> {
                                                newUser.lastMessage = "voice message"
                                                newUser.timeStamp =
                                                    tempList[0].timeStamp.toString()
                                            }
                                            TYPE_IMAGE -> {
                                                newUser.lastMessage = "image message"
                                                newUser.timeStamp =
                                                    tempList[0].timeStamp.toString()
                                            }
                                            TYPE_FILE -> {
                                                newUser.lastMessage = "file message"
                                                newUser.timeStamp =
                                                    tempList[0].timeStamp.toString()
                                            }
                                            else -> {
                                                newUser.lastMessage = tempList[0].text
                                                newUser.timeStamp =
                                                    tempList[0].timeStamp.toString()
                                            }
                                        }
                                        if (userList.isEmpty()) {
                                            userList.add(newUser)
                                        }
                                        userList.forEach {
                                            if (it.id == newUser.id) {
                                                it.lastMessage = newUser.lastMessage
                                                isNeedToAdd = false
                                            }
                                        }
                                        if (isNeedToAdd) {
                                            userList.add(newUser)
                                        }
                                        function(userList)
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

fun changeId(userId: String) {
    REF_DATABASE_ROOT.child(NODE_USERS_ID).child(userId).setValue(UID)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                updateCurrentUserId(userId)
            } else {
                showToast(it.exception?.message ?: "")
            }
        }
}

private fun updateCurrentUserId(userId: String) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_ID_NAME).setValue(userId)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                deleteOldUserId()
            } else {
                showToast(it.exception?.message ?: "")
            }
        }
}

private fun deleteOldUserId() {
    REF_DATABASE_ROOT.child(NODE_USERS_ID).child(USER.idName).removeValue()
        .addOnCompleteListener {
            if (it.isSuccessful) {

            } else {
                showToast(it.exception?.message ?: "")
            }
        }
}

fun updatePhonesList(arrayContacts: ArrayList<UserInfo>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES_NUMBER)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { snapshot ->
                        arrayContacts.forEach { contact ->
                            if (snapshot.key == contact.phone) {
                                REF_DATABASE_ROOT.child(NODE_PHONES_CONTACT).child(UID)
                                    .child(snapshot.value.toString()).child(CHILD_ID)
                                    .setValue(snapshot.value.toString())
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}


fun getContactListFromFirebase(): LiveData<List<UserInfo>> {
    val contactListLD = MutableLiveData<List<UserInfo>>()
    val contactsList = mutableListOf<UserInfo>()
    getContactIdList { list ->
        list.forEach { id ->
            REF_DATABASE_ROOT.child(NODE_USERS)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.map {

                            if (it.key == id) {
                                val contact = it.getValue(UserInfo::class.java) ?: UserInfo()
                                contactsList.add(contact)

                            }

                        }
                        contactListLD.value = contactsList

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }

    }
    return contactListLD
}

private fun getContactIdList(function: (MutableList<String>) -> Unit) {
    val contactIdList = mutableListOf<String>()
    REF_DATABASE_ROOT.child(NODE_PHONES_CONTACT).child(UID)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.map {
                    contactIdList.add(it.key.toString())
                }
                function(contactIdList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

}

