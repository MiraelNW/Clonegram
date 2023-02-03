package com.example.clonegram.utils

import android.widget.Toast

enum class UserState(val state: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает");

    companion object {
        fun updateState(userState: UserState) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_STATE)
                .setValue(userState.state)
                .addOnSuccessListener { USER.state = userState.state }
        }
    }
}