package com.alltrails.lunch.app.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.alltrails.lunch.app.network.Place
import com.alltrails.lunch.app.network.PlacesService
import com.alltrails.lunch.app.usecase.DisplayPreferences
import com.alltrails.lunch.app.usecase.FavoritesManager
import com.alltrails.lunch.app.usecase.LocationUpdatesUseCase
import kotlinx.coroutines.launch
import java.text.NumberFormat

@SuppressLint("MissingPermission")
class MainViewModel(
  private val placesService: PlacesService,
  private val locationProvider: LocationUpdatesUseCase,
  private val displayPreferences: DisplayPreferences,
  private val favoritesManager: FavoritesManager,
  ): BaseViewModel<RestaurantsViewState, MainViewModel.Action, MainViewModel.UiEvent>(
    RestaurantsViewState(
      showMap = displayPreferences.shouldShowMap,
      lat = 43.0,
      lon = -88.0,
      results = listOf(),
    )
  ) {

  init {
    viewModelScope.launch {
      locationProvider.getLastKnownLocation()?.let {
        state.dispatch(Action.OnLocationUpdated(it.latitude, it.longitude))
        handleQuerySubmitted("", it.latitude, it.longitude)
      }
    }
  }

  override fun reducer(state: RestaurantsViewState, action: Action): RestaurantsViewState {
    return when (action) {
      is Action.OnLocationUpdated -> state.copy(lat = action.lat, lon = action.lon)
      is Action.OnResultsUpdated -> state.copy(loading = false, results = action.restaurants.map {
        Restaurant(
          it.name,
          it.placeId,
          it.photos.firstOrNull()?.toImageUrl(),
          "${it.rating}",
          NumberFormat.getNumberInstance().format(it.ratingsCount),
          "support",
          favoritesManager.isFavorite(it.placeId),
          lat = it.geometry.location.lat,
          lon = it.geometry.location.lon
        )
      })
      is Action.OnFavoriteToggled -> {
        val alteredRestaurant = state.results.find {
          it.id == action.restaurantId
        }!!.copy(isFavorite = favoritesManager.isFavorite(action.restaurantId))

        state.copy(results = state.results.map {
          if (it.id == action.restaurantId) alteredRestaurant else it
        })
      }
      is Action.OnQuerySubmitted -> state.copy(loading = true)
    }
  }

  override fun handleUIEvent(event: UiEvent) {
    when(event) {
      is UiEvent.OnFavoriteToggled -> handleFavoriteToggled(event.id)
      is UiEvent.OnQuerySubmitted -> handleQuerySubmitted(event.query, event.lat, event.lon)
      is UiEvent.OnScreenToggled -> displayPreferences.shouldShowMap = event.shouldShowMap
    }
  }

  private fun handleFavoriteToggled(restaurantId: String) {
    favoritesManager.toggleFavorite(restaurantId)
    state.dispatch(Action.OnFavoriteToggled(restaurantId))
  }

  private fun handleQuerySubmitted(query: String, lat: Double, lon: Double) = viewModelScope.launch {
    state.dispatch(Action.OnQuerySubmitted)
    val results = placesService.nearbyRestaurants("$lat,$lon", query)
    state.dispatch(Action.OnResultsUpdated(results!!.results))
  }

  private fun Place.PlacePhoto.toImageUrl(): String {
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=64&maxheight=72&photo_reference=${reference}&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c"
  }

  sealed interface Action {
    data class OnLocationUpdated(val lat: Double, val lon: Double): Action
    data class OnResultsUpdated(val restaurants: List<Place>): Action
    data class OnFavoriteToggled(val restaurantId: String): Action
    object OnQuerySubmitted: Action
  }

  sealed interface UiEvent {
    data class OnScreenToggled(val shouldShowMap: Boolean): UiEvent
    data class OnFavoriteToggled(val id: String) : UiEvent
    data class OnQuerySubmitted(val query: String, val lat: Double, val lon: Double) : UiEvent
  }
}