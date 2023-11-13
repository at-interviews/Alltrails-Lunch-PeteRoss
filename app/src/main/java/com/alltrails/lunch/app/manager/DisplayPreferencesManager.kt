package com.alltrails.lunch.app.manager

import android.content.SharedPreferences
import androidx.core.content.edit

class DisplayPreferencesManager(
  private val sharedPreferences: SharedPreferences
) {

  var shouldShowMap: Boolean
    get() = sharedPreferences.getBoolean(PREF_KEY_SHOW_MAP, false)
    set(value) = sharedPreferences.edit { putBoolean(PREF_KEY_SHOW_MAP, value) }

  companion object {
    private const val PREF_KEY_SHOW_MAP = "showMap"
    const val PREF_NAME_DISPLAY_PREFS = "DisplayPreferences"
  }
}