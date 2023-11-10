package com.alltrails.lunch.app.network

data class PlacesResponse(val places: List<Place>)

data class Place(val name: String, val id: String, val types: List<String>)