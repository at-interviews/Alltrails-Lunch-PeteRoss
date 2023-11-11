package com.alltrails.lunch.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofitClient(): Retrofit {
  return Retrofit.Builder()
    .baseUrl("https://maps.googleapis.com/maps/api/place/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
}