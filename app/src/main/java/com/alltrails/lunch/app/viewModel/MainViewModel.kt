package com.alltrails.lunch.app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alltrails.lunch.app.network.NearbySearch
import com.alltrails.lunch.app.network.PlacesService
import kotlinx.coroutines.launch

class MainViewModel(private val placesService: PlacesService): ViewModel() {

  init {
    viewModelScope.launch {
      try {
        placesService.nearbyRestaurants(NearbySearch.create(1.35f, 103.87f))
      } catch (e: Exception) {
        Log.d("SLKDJLJK", "SLKDFJ")
      }
    }
  }
}