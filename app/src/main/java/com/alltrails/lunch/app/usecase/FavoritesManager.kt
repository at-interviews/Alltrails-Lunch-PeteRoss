package com.alltrails.lunch.app.usecase

import android.content.SharedPreferences
import androidx.core.content.edit

class FavoritesManager(
  private val sharedPreferences: SharedPreferences
) {

  fun isFavorite(restaurantId: String): Boolean {
    return sharedPreferences.getBoolean(restaurantId, false)
  }

  fun toggleFavorite(restaurantId: String) {
    val isCurrentlyFavorite = isFavorite(restaurantId = restaurantId)
    sharedPreferences.edit { putBoolean(restaurantId, !isCurrentlyFavorite) }
  }

  companion object {
    const val PREF_NAME_FAVORITES_MANAGER = "FavoritesManager"
  }
}