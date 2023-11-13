# Alltrails-Lunch


https://github.com/PeteRossPeteRoss/Alltrails-Lunch/assets/150725075/9113fa37-b820-4d49-9f6f-5b51cd6b1c6a


## Required Features
- [x] The app will use the Google Places API for its data source
- [x] The app will prompt the user for permission to access their current location
- [x] Upon launch, the app will execute a search that displays nearby restaurants
- [x] A search feature will be included that allows the user to search for restaurants
- [x] The user may choose to display the search results as a list, or as pins on a map
- [x] The user may select a search result to display basic information about the restaurant

## Bonus Points
- [x] Implement UI based on design requirements: UI Specifications
- [ ] Allow the user to flag restaurants as a favorite, and indicate its favorite status in the current and future search results
  - User can mark a restaurant as a favorite in the List view, but not the Map view. This is beause the InfoWindow that appears on the map for Google Maps is actually an _image_ of the Composable, not the actual composable, as [documented here](https://stackoverflow.com/questions/15924045/how-to-make-the-content-in-the-marker-info-window-clickable-in-android) and also not considered important to the future of Maps [documented here](https://github.com/googlemaps/android-maps-compose/issues/200).
     
