package com.alltrails.lunch.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme
import com.alltrails.lunch.app.ui.theme.Background
import com.alltrails.lunch.app.ui.theme.Padding1_5x
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.alltrails.lunch.app.ui.theme.PrimaryGreen
import com.alltrails.lunch.app.viewModel.MainViewModel
import com.alltrails.lunch.app.viewModel.Restaurant
import com.alltrails.lunch.app.viewModel.RestaurantsViewState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RestaurantsScreen(modifier: Modifier = Modifier, viewModel : MainViewModel = koinViewModel()) {
  val viewState: RestaurantsViewState by viewModel.viewState.collectAsState()
  
  RestaurantsScreen(
    modifier,
    viewState.lat,
    viewState.lon,
    viewState.results,
    viewModel::handleUIEvent
  )
}

@Composable
fun RestaurantsScreen(
  modifier: Modifier = Modifier,
  lat: Double,
  lon: Double,
  restaurants: List<Restaurant>,
  onUiEvent: (MainViewModel.UiEvent) -> Unit
) {
  Column(modifier = modifier
    .fillMaxSize()
    .background(Color.White)) {
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

    Button(onClick = { onUiEvent(MainViewModel.UiEvent.OnQuerySubmitted("Italian", lat, lon))}) {
      Text("Test the query")
    }


    var showMap by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
      if (showMap) {
        Map(lat, lon, restaurants)
      } else {
        List(restaurants)
      }

      val fabIcon = if (showMap) R.drawable.list else R.drawable.map
      val fabText = if (showMap) R.string.main_fab_list else R.string.main_fab_map
      val fabContentDescription = if (showMap) R.string.main_fab_content_description_show_list else R.string.main_fab_content_description_show_map
      ExtendedFloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(Padding1_5x)
        ,
        containerColor = PrimaryGreen,
        contentColor = Color.White,
        onClick = { showMap = !showMap },
        icon = { Icon(painter = painterResource(id = fabIcon), contentDescription = stringResource(
          id = fabContentDescription
        )) },
        text = { Text(text = stringResource(id = fabText)) },
      )
    }

  }
}

@Composable
private fun List(restaurants: List<Restaurant>) {
  LazyColumn(modifier = Modifier.background(Background)) {
    items(count = restaurants.size) {
      RestaurantListItem(
        restaurant = restaurants[it],
        modifier = Modifier.padding(horizontal = 24.dp, vertical = Padding1x)
      )
    }
  }
}

@Composable
private fun Map(
  lat: Double,
  lon: Double,
  restaurants: List<Restaurant>
) {
  val currentPosition = LatLng(lat, lon)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(currentPosition, 12f)
  }

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    cameraPositionState = cameraPositionState
  ) {
    restaurants.forEach { restaurant ->
      val context = LocalContext.current
      MarkerInfoWindow(
        state = MarkerState(position = LatLng(restaurant.lat, restaurant.lon)),
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
        }
      ) {
        RestaurantListItem(
          restaurant = restaurant,
          modifier = Modifier.padding(horizontal = 24.dp, vertical = Padding1x)
        )
      }
    }
  }
}

@Preview
@Composable
fun RestaurantsScreenPreview() {
  AllTrailsLunchTheme {
    RestaurantsScreen()
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