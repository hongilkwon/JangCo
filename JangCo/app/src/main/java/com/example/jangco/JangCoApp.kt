package com.example.jangco

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class JangCoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}