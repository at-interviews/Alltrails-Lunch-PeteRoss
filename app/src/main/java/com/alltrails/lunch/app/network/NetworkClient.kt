package com.alltrails.lunch.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofitClient(): Retrofit {
  return Retrofit.Builder()
    .baseUrl("https://places.googleapis.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
}