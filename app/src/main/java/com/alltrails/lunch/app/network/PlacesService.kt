package com.alltrails.lunch.app.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PlacesService {
  @Headers(
    "Content-Type: application/json",
    "X-Goog-Api-Key: AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c",
    "X-Goog-FieldMask: places.displayName,places.formattedAddress"
  )
  @POST("v1/places:searchNearby")
  suspend fun nearbyRestaurants(@Body nearbySearch: NearbySearch): PlacesResponse?
}