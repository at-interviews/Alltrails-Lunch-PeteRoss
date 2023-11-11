package com.alltrails.lunch.app.network

data class PlacesResponse(val results: List<Place>)

data class Place(val name: String, val placeId: String, val types: List<String>)