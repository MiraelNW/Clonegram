package com.example.clonegram.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabase {

    lateinit var AUTH: FirebaseAuth
    lateinit var REF_DATABASE_ROOT: DatabaseReference
    lateinit var UID: String

    private fun initFirebaseDatabase() {
        AUTH = FirebaseAuth.getInstance()
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
        UID = AUTH.currentUser?.uid.toString()
    }
    companion object{
        private const val NODE_USERS = "users"
        private const val CHILD_ID = "id"
        private const val CHILD_PHONE = "phone"
        private const val CHILD_USERNAME = "username"
        private const val CHILD_NAME = "name"
    }
}