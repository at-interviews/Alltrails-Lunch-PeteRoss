package com.alltrails.lunch.app.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.alltrails.lunch.app.network.Place
import com.alltrails.lunch.app.network.PlacesService
import com.alltrails.lunch.app.usecase.LocationUpdatesUseCase
import kotlinx.coroutines.launch
import java.text.NumberFormat

@SuppressLint("MissingPermission")
class MainViewModel(
  private val placesService: PlacesService,
  private val locationProvider: LocationUpdatesUseCase
  ): BaseViewModel<RestaurantsViewState, MainViewModel.Action, MainViewModel.UiEvent>(
    RestaurantsViewState(
      lat = 43.0,
      lon = -88.0,
      results = listOf()
    )
  ) {

  init {
    viewModelScope.launch {
      locationProvider.getLastKnownLocation()?.let {
        state.dispatch(Action.OnLocationUpdated(it.latitude, it.longitude))
      }
    }
  }

  override fun reducer(state: RestaurantsViewState, action: Action): RestaurantsViewState {
    return when (action) {
      is Action.OnLocationUpdated -> state.copy(lat = action.lat, lon = action.lon)
      is Action.OnResultsUpdated -> state.copy(results = action.restaurants.map {
        Restaurant(
          it.name,
          it.placeId,
          it.photos.first().toImageUrl(),
          "${it.rating}",
          NumberFormat.getNumberInstance().format(it.ratingsCount),
          "support",
          false,
          lat = it.geometry.location.lat,
          lon = it.geometry.location.lon
        )
      })
    }
  }

  override fun handleUIEvent(event: UiEvent) {
    when(event) {
      is UiEvent.OnFavoriteToggled ->Unit // TODO()
      is UiEvent.OnQuerySubmitted -> handleQuerySubmitted(event.query, event.lat, event.lon)
      UiEvent.OnScreenToggled -> Unit // TODO()
    }
  }

  private fun handleQuerySubmitted(query: String, lat: Double, lon: Double) = viewModelScope.launch {
    val results = placesService.nearbyRestaurants("$lat,$lon", query)
    state.dispatch(MainViewModel.Action.OnResultsUpdated(results!!.results))
  }

  private fun Place.PlacePhoto.toImageUrl(): String {
    return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${reference}&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c"
  }

  sealed interface Action {
    data class OnLocationUpdated(val lat: Double, val lon: Double): Action
    data class OnResultsUpdated(val restaurants: List<Place>): Action
  }

  sealed interface UiEvent {
    object OnScreenToggled: UiEvent
    data class OnFavoriteToggled(val id: String, val favorite: Boolean) : UiEvent
    data class OnQuerySubmitted(val query: String, val lat: Double, val lon: Double) : UiEvent
  }
}