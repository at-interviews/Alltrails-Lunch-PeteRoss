package com.alltrails.lunch.app.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alltrails.lunch.app.ui.theme.Padding1x

@Composable
fun RestaurantListItem(
  imageUrl: String?,
  restaurantName: String,
  rating: String,
  ratingsCount: String,
  supportingText: String,
  isFavorite: Boolean,
) {
  Card {
    Row(modifier = Modifier.padding(Padding1x)) {

    }
  }
}

@Composable
@Preview
fun RestaurantListItemPreview() {
  RestaurantListItem(
    imageUrl = "imageUrl",
    restaurantName = "restaurantName",
    rating = "rating",
    ratingsCount = "ratingsCount",
    supportingText = "supportingText",
    isFavorite = true,
  )
}