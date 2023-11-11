package com.alltrails.lunch.app.network

import com.google.gson.annotations.SerializedName

data class PlacesResponse(val results: List<Place>)

data class Place(
  val name: String,
  val photos: List<PlacePhoto>,
  val rating: Float,
  @SerializedName("user_ratings_total") val ratingsCount: Int,
  @SerializedName("place_id") val placeId: String,
  val types: List<String>,
  val geometry: Geometry
) {
  data class Geometry(val location: LatLon)

  data class PlacePhoto(
    @SerializedName("photo_reference") val reference: String
  )
}

data class LatLon(val lat: Double, @SerializedName("lng") val lon: Double)