package com.example.clonegram

import android.app.Application
import com.example.clonegram.di.DaggerApplicationComponent

class ClonegramApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()

    }

}