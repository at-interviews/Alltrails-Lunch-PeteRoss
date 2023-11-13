package com.alltrails.lunch.app.usecase

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationUseCase(
  private val client: FusedLocationProviderClient
) {

  @SuppressLint("MissingPermission")
  suspend operator fun invoke(): Location? = suspendCancellableCoroutine { continuation ->
    client.lastLocation.apply {
      addOnSuccessListener { continuation.resumeWith(Result.success(it)) }
      addOnFailureListener { continuation.resumeWith(Result.failure(it)) }
    }
  }
}