package com.example.geeta

import android.app.Application
import com.google.android.material.color.DynamicColors

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)

    }
}