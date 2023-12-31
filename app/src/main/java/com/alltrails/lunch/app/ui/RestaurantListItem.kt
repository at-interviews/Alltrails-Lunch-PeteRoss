package com.alltrails.lunch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.alltrails.lunch.app.ui.theme.PaddingInner
import com.alltrails.lunch.app.ui.theme.RestaurantItemImageHeight
import com.alltrails.lunch.app.ui.theme.RestaurantItemImageWidth
import com.alltrails.lunch.app.ui.theme.SpacingSmall
import com.alltrails.lunch.app.viewstate.Restaurant

@Composable
fun RestaurantListItem(
  restaurant: Restaurant,
  onFavoriteClicked: (String) -> Unit,
  modifier: Modifier = Modifier
  ) {
  RestaurantListItem(
    id = restaurant.id,
    imageUrl = restaurant.imageUrl,
    restaurantName = restaurant.name,
    rating = restaurant.rating,
    ratingsCount = restaurant.ratingsCount,
    supportingText = stringResource(id = restaurant.supportingTextRes),
    isFavorite = restaurant.isFavorite,
    onFavoriteClicked = onFavoriteClicked,
    modifier = modifier,
  )
}

@Composable
fun RestaurantListItem(
  id: String,
  imageUrl: String?,
  restaurantName: String,
  rating: String,
  ratingsCount: String,
  supportingText: String,
  isFavorite: Boolean,
  onFavoriteClicked: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ElevatedCard(
    elevation = CardDefaults.cardElevation(
      defaultElevation = 16.dp
    ),
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = Color.White,
    ),
  ) {
    Row(
      modifier = Modifier
        .padding(Padding1x)
        .fillMaxWidth()
    ) {
      val request = ImageRequest.Builder(LocalContext.current).data(imageUrl).allowHardware(false).build()

      AsyncImage(
        modifier = Modifier
          .width(RestaurantItemImageWidth)
          .height(RestaurantItemImageHeight),
        model = request,
        contentScale = ContentScale.FillHeight,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.placeholder_image)
      )
      Spacer(modifier = Modifier.width(PaddingInner))
      Column {
        Row(verticalAlignment = Alignment.Top) {
          Text(
            text = restaurantName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
          )

          val imageRes = if (isFavorite) R.drawable.bookmark_saved else R.drawable.bookmark_resting
          val contentDescription = if (isFavorite) {
            R.string.bookmark_saved_icon_content_description
          } else {
            R.string.bookmark_resting_icon_content_description
          }
          Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(id = contentDescription),
            modifier = Modifier.clickable { onFavoriteClicked(id) },
          )
        }

        Spacer(modifier = Modifier.height(SpacingSmall))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = null
          )

          Spacer(modifier = Modifier.width(2.dp))

          Text(
            text = rating,
            style = MaterialTheme.typography.titleSmall
          )
          Text(
            text = stringResource(id = R.string.bullet_separator),
            modifier = Modifier.padding(horizontal = 4.dp)
          )
          Text(
            text = stringResource(id = R.string.restaurant_ratings_count_wrapper, ratingsCount),
            style = MaterialTheme.typography.bodySmall
          )
        }

        Spacer(modifier = Modifier.height(SpacingSmall))

        Text(text = supportingText, style = MaterialTheme.typography.bodySmall)
      }
    }
  }
}

@Composable
@Preview
fun RestaurantListItemPreview() {
  AllTrailsLunchTheme {
    RestaurantListItem(
      id = "id",
      imageUrl = "imageUrl",
      restaurantName = "Cellar Door Provisions",
      rating = "4.8",
      ratingsCount = "1,324",
      supportingText = "It's great!",
      isFavorite = false,
      onFavoriteClicked = {}
    )
  }
}