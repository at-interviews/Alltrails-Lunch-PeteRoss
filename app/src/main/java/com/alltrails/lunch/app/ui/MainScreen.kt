package com.alltrails.lunch.app.ui

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
  Scaffold {
    Column(modifier = Modifier.padding(it)) {
      val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION
        )
      )

      if (locationPermissionsState.allPermissionsGranted) {
        Text(text = "GRANTED")
      } else if (locationPermissionsState.shouldShowRationale) {
        Column {
          Text("You can't find nearby restaurants if we don't know where you are! Please enable location permissions.")
          Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
            Text("Request permission")
          }
        }
      } else {
        LaunchedEffect(key1 = "Permission", block = { locationPermissionsState.launchMultiplePermissionRequest() })
      }
    }
  }
}