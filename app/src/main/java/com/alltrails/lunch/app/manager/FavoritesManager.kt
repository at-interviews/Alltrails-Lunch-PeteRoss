package com.alltrails.lunch.app.manager

import android.content.SharedPreferences
import androidx.core.content.edit

class FavoritesManager(
  private val sharedPreferences: SharedPreferences
) {

  fun isFavorite(restaurantId: String): Boolean {
    return sharedPreferences.getBoolean(restaurantId, false)
  }

  fun toggleFavorite(restaurantId: String) {
    sharedPreferences.edit { putBoolean(restaurantId, !isFavorite(restaurantId = restaurantId)) }
  }

  companion object {
    const val PREF_NAME_FAVORITES_MANAGER = "FavoritesManager"
  }
}