package com.alltrails.lunch.app.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.alltrails.lunch.app.usecase.DisplayPreferencesManager
import com.alltrails.lunch.app.usecase.FavoritesManager
import com.alltrails.lunch.app.usecase.FindRestaurantsUseCase
import com.alltrails.lunch.app.usecase.LocationUseCase
import com.alltrails.lunch.app.viewstate.Restaurant
import com.alltrails.lunch.app.viewstate.RestaurantsViewState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class RestaurantsViewModel(
  private val findRestaurants: FindRestaurantsUseCase,
  private val currentLocation: LocationUseCase,
  private val displayPreferencesManager: DisplayPreferencesManager,
  private val favoritesManager: FavoritesManager,
  ): BaseViewModel<RestaurantsViewState, RestaurantsViewModel.Action, RestaurantsViewModel.UiEvent>(
    RestaurantsViewState(
      showMap = displayPreferencesManager.shouldShowMap,
      lat = 43.0,
      lon = -88.0,
    )
  ) {

  init {
    viewModelScope.launch {
      currentLocation()?.let {
        state.dispatch(Action.OnLocationUpdated(it.latitude, it.longitude))
        handleQuerySubmitted("", it.latitude, it.longitude)
      }
    }
  }

  override fun reducer(state: RestaurantsViewState, action: Action): RestaurantsViewState {
    return when (action) {
      is Action.OnLocationUpdated -> state.copy(lat = action.lat, lon = action.lon)
      is Action.OnResultsUpdated -> state.copy(loading = false, results = action.restaurants )
      is Action.OnFavoriteToggled -> {
        state.copy(
          results = state.results.map {
            if (it.id == action.restaurantId) {
              it.copy(isFavorite = favoritesManager.isFavorite(action.restaurantId))
            } else {
              it
            }
          }
        )
      }
      is Action.LoadingResults -> state.copy(loading = true)
      is Action.MapMoved -> state.copy(lat = action.latlng.latitude, lon = action.latlng.longitude, zoom = action.zoom)
    }
  }

  override fun handleUIEvent(event: UiEvent) {
    when(event) {
      is UiEvent.OnFavoriteToggled -> handleFavoriteToggled(event.id)
      is UiEvent.OnQuerySubmitted -> handleQuerySubmitted(event.query, event.lat, event.lon)
      is UiEvent.OnScreenToggled -> displayPreferencesManager.shouldShowMap = event.shouldShowMap
      is UiEvent.OnMapMoved -> state.dispatch(Action.MapMoved(event.latlng, event.zoom))
    }
  }

  private fun handleFavoriteToggled(restaurantId: String) {
    favoritesManager.toggleFavorite(restaurantId)
    state.dispatch(Action.OnFavoriteToggled(restaurantId))
  }

  private fun handleQuerySubmitted(query: String, lat: Double, lon: Double) = viewModelScope.launch {
    state.dispatch(Action.LoadingResults)
    val results = findRestaurants(query, lat, lon)
    state.dispatch(Action.OnResultsUpdated(results))
  }

  sealed interface Action {
    data class OnLocationUpdated(val lat: Double, val lon: Double): Action
    data class OnResultsUpdated(val restaurants: List<Restaurant>): Action
    data class OnFavoriteToggled(val restaurantId: String): Action
    data class MapMoved(val latlng: LatLng, val zoom: Float): Action
    object LoadingResults: Action
  }

  sealed interface UiEvent {
    data class OnScreenToggled(val shouldShowMap: Boolean): UiEvent
    data class OnFavoriteToggled(val id: String) : UiEvent
    data class OnQuerySubmitted(val query: String, val lat: Double, val lon: Double) : UiEvent
    data class OnMapMoved(val latlng: LatLng, val zoom: Float) : UiEvent
  }
}