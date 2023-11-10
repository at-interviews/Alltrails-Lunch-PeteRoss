package com.alltrails.lunch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.Padding1_5x
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.alltrails.lunch.app.ui.theme.SearchbarHeight
import com.alltrails.lunch.app.viewModel.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel : MainViewModel = koinViewModel()) {
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
        SearchBar(
          query = "ffdr",
          onQueryChange = { /* No autocomplete for now */ },
          onSearch = {},
          modifier = Modifier
            .padding(
              start = Padding1_5x,
              end = Padding1_5x,
              bottom = Padding1x,
            )
            .clip(shape = RoundedCornerShape(Padding1x))
            .height(SearchbarHeight)
          ,
          placeholder = {
            Text(text = "Search restaurants")
          },
          active = true,
          onActiveChange = {},
          leadingIcon = { Image(painter = painterResource(id = R.drawable.search), null)}
        ) {}
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