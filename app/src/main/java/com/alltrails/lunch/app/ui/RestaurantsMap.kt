package com.alltrails.lunch.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.alltrails.lunch.app.viewstate.Restaurant
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun RestaurantsMap(
  lat: Double,
  lon: Double,
  zoom: Float,
  restaurants: List<Restaurant>,
  onFavoriteClick: (String) -> Unit,
  onMapMoved: (LatLng, Float) -> Unit,
) {
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(LatLng(lat, lon), zoom)
  }

  LaunchedEffect(cameraPositionState.isMoving) {
    if (!cameraPositionState.isMoving) {
      onMapMoved(cameraPositionState.position.target, cameraPositionState.position.zoom)
    }
  }

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState,

    ) {
    restaurants.forEach { restaurant ->
      val context = LocalContext.current
      MarkerInfoWindow(
        state = rememberMarkerState(position = LatLng(restaurant.lat, restaurant.lon)),
        icon = bitmapDescriptorFromVector(context, R.drawable.pin_resting),
        onInfoWindowClose = {
          it.setIcon(
            bitmapDescriptorFromVector(
              context,
              R.drawable.pin_resting
            )
          )
        },
        onClick = {
          it.setIcon(bitmapDescriptorFromVector(context, R.drawable.pin_selected))
          false
        },
        onInfoWindowClick = {
          Toast.makeText(context, R.string.map_popover_cannot_bookmark_message, Toast.LENGTH_SHORT).show()
        }
      ) {
        // Using this Compose GoogleMap, you are unable to interact with the content within a window
        // https://github.com/googlemaps/android-maps-compose/issues/200
        // This is because the Map renders the Composable as an image, as described here
        // https://stackoverflow.com/questions/15924045/how-to-make-the-content-in-the-marker-info-window-clickable-in-android
        RestaurantListItem(
          restaurant = restaurant,
          onFavoriteClicked = onFavoriteClick,
          modifier = Modifier.padding(horizontal = 24.dp, vertical = Padding1x)
        )
      }
    }
  }
}

private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
  return ContextCompat.getDrawable(context, vectorResId)?.run {
    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    draw(Canvas(bitmap))
    BitmapDescriptorFactory.fromBitmap(bitmap)
  }
}

@Preview
@Composable
fun MapScreenPreview() {
  AllTrailsLunchTheme {
    RestaurantsMap(
      43.0,
      -88.0,
      12f,
      listOf(),
      {},
      {_, _ -> }
    )
  }
}
