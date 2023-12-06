package com.stardust.autojs.core.pref

import androidx.preference.PreferenceManager
import com.stardust.app.GlobalAppContext

object Pref {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAppContext.get())

    val isGestureObservingEnabled: Boolean
        get() {
            return preferences.getBoolean("key_gesture_observing", false)
        }
}