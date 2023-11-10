package com.alltrails.lunch.app.ui

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

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
  ) {
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
          Text(stringResource(id = R.string.permission_denied_explainer))
          Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
            Text(stringResource(id = R.string.request_location_permissions_button))
          }
        }
      } else {
        LaunchedEffect("Permission") {
          locationPermissionsState.launchMultiplePermissionRequest()
        }
      }
    }
  }
}

@Preview
@Composable
private fun MainScreenPreview() {
  MainScreen()
}