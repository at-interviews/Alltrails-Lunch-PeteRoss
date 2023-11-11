package com.alltrails.lunch.app.usecase

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationUpdatesUseCase(
  private val client: FusedLocationProviderClient
) {

  @SuppressLint("MissingPermission")
  suspend fun getLastKnownLocation(): Location? = suspendCancellableCoroutine { continuation ->
    client.lastLocation.apply {
      addOnSuccessListener { continuation.resumeWith(Result.success(it)) }
      addOnFailureListener { continuation.resumeWith(Result.failure(it)) }
    }
  }

  companion object {
    private const val UPDATE_INTERVAL_SECS = 10L
    private const val FASTEST_UPDATE_INTERVAL_SECS = 2L
  }
}