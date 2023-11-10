package com.alltrails.lunch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme
import com.alltrails.lunch.app.ui.theme.Background
import com.alltrails.lunch.app.ui.theme.Padding1_5x
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun RestaurantsScreen(modifier: Modifier = Modifier) {
  val singapore = LatLng(1.35, 103.87)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(singapore, 10f)
  }
  Column(modifier = modifier.fillMaxSize().background(Color.White)) {
    TextField(
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          start = Padding1_5x,
          end = Padding1_5x,
          bottom = Padding1x,
        )
        .clip(shape = RoundedCornerShape(Padding1x))
        .background(Background)
//        .height(SearchbarHeight)
      ,
      value = "",
      leadingIcon = { Image(painter = painterResource(id = R.drawable.search), null) },
      onValueChange = {},
      placeholder = { Text(text = "Search restaurants") }
    )

    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState
    )
  }
}

@Preview
@Composable
fun RestaurantsScreenPreview() {
  AllTrailsLunchTheme {
    RestaurantsScreen()
  }
}