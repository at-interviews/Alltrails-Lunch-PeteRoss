package com.alltrails.lunch.app.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.viewModel.Restaurant

class RestaurantsProvider: PreviewParameterProvider<List<Restaurant>> {
  override val values: Sequence<List<Restaurant>>
    get() = sequenceOf(
      listOf(
        Restaurant(
          name = "name",
          id = "id",
          imageUrl = "imageUrl",
          rating = "rating",
          ratingsCount = "ratingsCount",
          supportingTextRes = R.string.restaurant_open,
          isFavorite = true,
          lat = 0.0,
          lon = 0.0,
        ),
        Restaurant(
          name = "McDonalds",
          id = "id2",
          imageUrl = "imageUrl",
          rating = "3.2",
          ratingsCount = "10,000",
          supportingTextRes = R.string.restaurant_open,
          isFavorite = false,
          lat = 0.0,
          lon = 0.0,
        ),
        Restaurant(
          name = "Uptown Clubist",
          id = "id3",
          imageUrl = "imageUrl",
          rating = "5.0",
          ratingsCount = "3",
          supportingTextRes = R.string.restaurant_closed,
          isFavorite = true,
          lat = 0.0,
          lon = 0.0,
        ),
      )
    )
}