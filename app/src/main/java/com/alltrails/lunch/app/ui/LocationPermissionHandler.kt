package com.alltrails.lunch.app.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
  onPermissionDenied: @Composable (modifier: Modifier, onPermissionRequested: () -> Unit) -> Unit,
  onPermissionsRevoked: @Composable (modifier: Modifier, navigateToSettingsClick: () -> Unit) -> Unit,
  onPermissionGranted: @Composable (Modifier) -> Unit,
  modifier: Modifier = Modifier,
) {
  val locationPermissionsState = rememberMultiplePermissionsState(
    permissions = listOf(
      Manifest.permission.ACCESS_COARSE_LOCATION,
      Manifest.permission.ACCESS_FINE_LOCATION
    )
  )

  LaunchedEffect(locationPermissionsState) {
    locationPermissionsState.launchMultiplePermissionRequest()
  }

  if (locationPermissionsState.allPermissionsGranted) {
    onPermissionGranted(modifier)
  } else if (locationPermissionsState.shouldShowRationale) {
    onPermissionDenied(modifier) { locationPermissionsState.launchMultiplePermissionRequest() }
  } else if (locationPermissionsState.revokedPermissions.size == 2) {
    val activity = LocalContext.current as Activity
    onPermissionsRevoked(modifier) {
      activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1)
    }
  }
}