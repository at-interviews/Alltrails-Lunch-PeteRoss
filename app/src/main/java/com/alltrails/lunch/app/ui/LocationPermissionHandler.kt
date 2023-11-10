package com.alltrails.lunch.app.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
  onPermissionDenied : @Composable (modifier: Modifier, onPermissionRequested: () -> Unit) -> Unit,
  onPermissionGranted : @Composable (Modifier) -> Unit,
  modifier: Modifier = Modifier,
  ) {
//  Column(modifier = modifier) {
    val locationPermissionsState = rememberMultiplePermissionsState(
      permissions = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
    )

    if (locationPermissionsState.allPermissionsGranted) {
      onPermissionGranted(modifier)
    } else if (locationPermissionsState.shouldShowRationale) {
      onPermissionDenied(modifier) { locationPermissionsState.launchMultiplePermissionRequest() }
    } else {
      LaunchedEffect("Permission") {
        locationPermissionsState.launchMultiplePermissionRequest()
      }
    }
//  }
}