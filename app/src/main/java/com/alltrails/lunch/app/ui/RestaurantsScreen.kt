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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RestaurantsScreen(modifier: Modifier = Modifier, viewModel : MainViewModel = koinViewModel()) {
  val viewState: RestaurantsViewState by viewModel.viewState.collectAsState()
  
  RestaurantsScreen(
    modifier,
    viewState.showMap,
    viewState.loading,
    viewState.lat,
    viewState.lon,
    viewState.zoom,
    viewState.results,
    viewModel::handleUIEvent
  )
}

@Composable
fun RestaurantsScreen(
  modifier: Modifier = Modifier,
  shouldShowMapInitially: Boolean,
  isLoading: Boolean,
  lat: Double,
  lon: Double,
  zoom: Float,
  restaurants: List<Restaurant>,
  onUiEvent: (MainViewModel.UiEvent) -> Unit
) {
  Column(modifier = modifier
    .fillMaxSize()
    .background(Color.White)
  ) {
    SearchBar(lat, lon, onUiEvent)

    Divider()
    var showMap by remember { mutableStateOf(shouldShowMapInitially) }
    val onFavoriteClick: (String) -> Unit = { onUiEvent(MainViewModel.UiEvent.OnFavoriteToggled(it)) }

    Box(modifier = Modifier.fillMaxSize()) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier
            .width(64.dp)
            .align(Alignment.Center),
          color = MaterialTheme.colorScheme.surfaceVariant,
          trackColor = MaterialTheme.colorScheme.secondary,
        )
      } else if (showMap) {
        Map(lat, lon, zoom, restaurants, onFavoriteClick) { latlng, zoom ->
          onUiEvent(MainViewModel.UiEvent.OnMapMoved(latlng, zoom))
        }
      } else {
        List(restaurants, onFavoriteClick)
      }

      val fabIcon = if (showMap) R.drawable.list else R.drawable.map
      val fabText = if (showMap) R.string.main_fab_list else R.string.main_fab_map
      val fabContentDescription = if (showMap) R.string.main_fab_content_description_show_list else R.string.main_fab_content_description_show_map
      ExtendedFloatingActionButton(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(Padding1_5x)
        ,
        shape = RoundedCornerShape(percent = 50),
        containerColor = PrimaryGreen,
        contentColor = Color.White,
        onClick = {
          showMap = !showMap
          onUiEvent(MainViewModel.UiEvent.OnScreenToggled(showMap))
                  },
        icon = { Icon(painter = painterResource(id = fabIcon), contentDescription = stringResource(
          id = fabContentDescription
        )) },
        text = { Text(text = stringResource(id = fabText)) },
      )
    }
  }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
private fun SearchBar(
  lat: Double,
  lon: Double,
  onUiEvent: (MainViewModel.UiEvent) -> Unit,
) {

  val keyboardController = LocalSoftwareKeyboardController.current
  var query: String by remember { mutableStateOf("") }
  TextField(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        start = Padding1_5x,
        end = Padding1_5x,
        bottom = Padding1x,
      ),
    value = query,
    shape = CircleShape,
    leadingIcon = { Image(painter = painterResource(id = R.drawable.search), null) },
    onValueChange = { query = it },
    placeholder = { Text(text = stringResource(id = R.string.search_bar_hint_text)) },
    singleLine = true,
    keyboardActions = KeyboardActions(
      onSearch = {
        onUiEvent(MainViewModel.UiEvent.OnQuerySubmitted(query, lat, lon))
        keyboardController?.hide()
      }
    ),
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    colors = TextFieldDefaults.colors(
      disabledTextColor = Color.Transparent,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent
    )
  )
}

@Composable
private fun List(
  restaurants: List<Restaurant>,
  onFavoriteClick: (String) -> Unit,
  ) {
  LazyColumn(modifier = Modifier.background(Background)) {
    items(count = restaurants.size) {
      RestaurantListItem(
        restaurant = restaurants[it],
        onFavoriteClicked = onFavoriteClick,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = Padding1x)
      )
    }
  }
}

@Composable
private fun Map(
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

//@Preview
//@Composable
//fun RestaurantsScreenPreview() {
//  AllTrailsLunchTheme {
//    RestaurantsScreen()
//  }
//}

@Preview
@Composable
fun SearchbarPreview() {
  AllTrailsLunchTheme {
    SearchBar(0.0, 0.0) {}
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