package com.example.clonegram.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.clonegram.domain.models.UserInfo
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var UID: String
lateinit var USER: UserInfo

const val NODE_USERS = "users"
const val NODE_USERS_ID = "usersid"

const val FOLDER_PROFILE_IMAGE = "profile_image"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_NAME = "name"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO = "photoUrl"

fun initFirebaseDatabase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    UID = AUTH.currentUser?.uid.toString()
    USER = UserInfo()
}
