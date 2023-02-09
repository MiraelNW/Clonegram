package com.example.clonegram.utils

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun ImageView.downloadAndSetImage(url:String){
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.user)
        .into(this )
}

fun String.time():String{
    if(this.isNotEmpty()){
        val time = Date(this.toLong())
        val timeFormat =  SimpleDateFormat("HH:mm",Locale.getDefault())
        return timeFormat.format(time)
    }
    return ""
}

