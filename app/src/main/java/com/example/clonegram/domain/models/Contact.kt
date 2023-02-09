package com.example.clonegram.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    var id:String="",
    var name:String ="",
    var photoUrl: String="empty",
    var phone:String="",
):Parcelable