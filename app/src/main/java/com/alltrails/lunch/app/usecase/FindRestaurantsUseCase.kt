package com.alltrails.lunch.app.usecase

import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.network.Place
import com.alltrails.lunch.app.network.PlacesService
import com.alltrails.lunch.app.viewModel.Restaurant
import java.text.NumberFormat

class FindRestaurantsUseCase(
  private val placesService: PlacesService,
  private val favoritesManager: FavoritesManager,
) {
  suspend operator fun invoke(query: String?, lat: Double, lon: Double): List<Restaurant> {
    return placesService.nearbyRestaurants("$lat,$lon", query.orEmpty())?.results?.map {
        Restaurant(
          it.name,
          it.placeId,
          it.photos.firstOrNull()?.toImageUrl(),
          "${it.rating}",
          NumberFormat.getNumberInstance().format(it.ratingsCount),
          if (it.openingHours?.openNow == true) R.string.restaurant_open else R.string.restaurant_closed,
          favoritesManager.isFavorite(it.placeId),
          lat = it.geometry.location.lat,
          lon = it.geometry.location.lon
        )
    } ?: emptyList()
  }
}

private fun Place.PlacePhoto.toImageUrl(): String {
  return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=64&maxheight=72&photo_reference=${reference}&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c"
}