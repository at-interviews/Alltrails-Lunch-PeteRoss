package com.alltrails.lunch.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alltrails.lunch.app.R
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme
import com.alltrails.lunch.app.ui.theme.Padding1x
import com.alltrails.lunch.app.ui.theme.PaddingInner
import com.alltrails.lunch.app.ui.theme.RestaurantItemImageHeight
import com.alltrails.lunch.app.ui.theme.RestaurantItemImageWidth
import com.alltrails.lunch.app.ui.theme.SpacingSmall

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
    Row(
      modifier = Modifier
        .padding(Padding1x)
        .fillMaxWidth()
    ) {
      AsyncImage(
        modifier = Modifier
          .width(RestaurantItemImageWidth)
          .height(RestaurantItemImageHeight),
        model = imageUrl,
        contentDescription = null,
        placeholder = painterResource(id = R.drawable.placeholder_image)
      )
      Spacer(modifier = Modifier.width(PaddingInner))
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
            contentDescription = stringResource(id = contentDescription)
          )
        }

        Spacer(modifier = Modifier.height(SpacingSmall))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = null
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(text = rating, style = MaterialTheme.typography.titleSmall)
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = stringResource(id = R.string.bullet_separator))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = ratingsCount, style = MaterialTheme.typography.bodySmall)
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
      imageUrl = "imageUrl",
      restaurantName = "Cellar Door Provisions",
      rating = "4.8",
      ratingsCount = "(1,324)",
      supportingText = "It's great!",
      isFavorite = false,
    )
  }
}