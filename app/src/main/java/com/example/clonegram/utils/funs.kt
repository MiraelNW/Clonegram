package com.example.clonegram.utils

import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clonegram.R
import com.squareup.picasso.Picasso

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