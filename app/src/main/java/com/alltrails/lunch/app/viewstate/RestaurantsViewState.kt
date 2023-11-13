package com.alltrails.lunch.app.viewstate

import androidx.annotation.StringRes

data class RestaurantsViewState(
  val loading: Boolean = true,
  val showMap: Boolean = false,
  val lat: Double,
  val lon: Double,
  val zoom: Float = 12f,
  val results: List<Restaurant> = listOf()
)

data class Restaurant(
  val name: String,
  val id: String,
  val imageUrl: String?,
  val rating: String,
  val ratingsCount: String,
  @StringRes val supportingTextRes: Int,
  val isFavorite: Boolean,
  val lat: Double,
  val lon: Double,
)