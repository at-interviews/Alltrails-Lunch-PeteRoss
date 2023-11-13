package com.alltrails.lunch.app.ui

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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.PrimaryGreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
  SetStatusBarColor(color = PrimaryGreen)
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
              painter = painterResource(id = R.drawable.logo),
              contentDescription = null,
            )
          }
        }
      )
    }
  ) { padding ->
    LocationPermissionHandler(
      modifier = Modifier.padding(padding),
      onPermissionDenied = { modifier, onPermissionRequested ->
        Column(modifier = modifier) {
          Text(stringResource(id = R.string.permission_denied_explainer))
          Button(onClick = onPermissionRequested) {
            Text(stringResource(id = R.string.request_location_permissions_button))
          }
        }
      },
      onPermissionGranted = { modifier ->
        RestaurantsScreen(modifier)
      }
    )
  }
}

@Composable
fun SetStatusBarColor(color: Color) {
  val systemUiController = rememberSystemUiController()
  SideEffect {
    systemUiController.setSystemBarsColor(color)
  }
}

@Preview
@Composable
private fun MainScreenPreview() {
  MainScreen()
}