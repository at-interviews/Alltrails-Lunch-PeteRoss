package com.alltrails.lunch.app.viewModel

data class RestaurantsViewState(
  val loading: Boolean = true,
  val lat: Double,
  val lon: Double,
  val results: List<Restaurant>
)

data class Restaurant(
  val name: String,
  val id: String,
  val imageUrl: String?,
  val rating: String,
  val ratingsCount: String,
  val supportingText: String,
  val isFavorite: Boolean,
  val lat: Double,
  val lon: Double,
)