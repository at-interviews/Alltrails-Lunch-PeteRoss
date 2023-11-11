package com.alltrails.lunch.app.network

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {
  @GET("nearbysearch/json?radius=5000&type=restaurant&key=AIzaSyCqWHKkgLxJiSwS63bxfWpQ-XhSQs65H5c")
  suspend fun nearbyRestaurants(
    @Query("location") location: String,
    @Query("keyword") query: String
  ): PlacesResponse?
}