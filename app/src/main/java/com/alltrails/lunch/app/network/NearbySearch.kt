package com.alltrails.lunch.app.network

data class NearbySearch(
  val includedTypes: List<String> = listOf("restaurant"),
  val maxResultCount: Int = 20,
  val locationRestriction: Location
) {

  companion object {
    fun create(lat: Float, long: Float): NearbySearch {
      return NearbySearch(
        locationRestriction = Location(
          Location.Circle(
            center = Location.Circle.Center(lat, long)
          )
        )
      )
    }
  }
  data class Location(val circle: Circle) {
    data class Circle(
      val radius: Int = 500,
      val center: Center,
    ) {
      data class Center(val latitude: Float, val longitude: Float)
    }
  }
}

//"includedTypes": ["restaurant"],
//"maxResultCount": 10,
//"locationRestriction": {
//  "circle": {
//    "center": {
//    "latitude": 37.7937,
//    "longitude": -122.3965},
//    "radius": 500.0
//  }
//}