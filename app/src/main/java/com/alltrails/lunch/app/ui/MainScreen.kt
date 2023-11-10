package com.alltrails.lunch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Image(
              modifier = Modifier.wrapContentSize(),
              painter = painterResource(id = R.drawable.logo_lockup),
              contentDescription = null,
            )
          }
        }
      )
    }
  ) { padding ->
    LocationPermissionHandler(
      modifier = Modifier.padding(padding),
      onPermissionDenied = {
        Column {
          Text(stringResource(id = R.string.permission_denied_explainer))
          Button(onClick = it) {
            Text(stringResource(id = R.string.request_location_permissions_button))
          }
        }
      },
      onPermissionGranted =  {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
          position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
          modifier = Modifier.fillMaxSize(),
          cameraPositionState = cameraPositionState
        )
      }
    )
  }
}

@Preview
@Composable
private fun MainScreenPreview() {
  MainScreen()
}