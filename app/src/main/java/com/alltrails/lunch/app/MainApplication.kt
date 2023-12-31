package com.alltrails.lunch.app

import android.app.Application
import com.alltrails.lunch.app.manager.DisplayPreferencesManager
import com.alltrails.lunch.app.manager.DisplayPreferencesManager.Companion.PREF_NAME_DISPLAY_PREFS
import com.alltrails.lunch.app.manager.FavoritesManager
import com.alltrails.lunch.app.manager.FavoritesManager.Companion.PREF_NAME_FAVORITES_MANAGER
import com.alltrails.lunch.app.network.PlacesService
import com.alltrails.lunch.app.network.createRetrofitClient
import com.alltrails.lunch.app.usecase.FindRestaurantsUseCase
import com.alltrails.lunch.app.usecase.LocationUseCase
import com.alltrails.lunch.app.viewModel.RestaurantsViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

class MainApplication: Application() {
  override fun onCreate() {
    super.onCreate()
    initDependencyInjection()

  }

  private fun initDependencyInjection() {
    startKoin {
      androidLogger()
      androidContext(this@MainApplication)
      modules(
        module {
          viewModelOf(::RestaurantsViewModel)
          single(createdAtStart = true) { createRetrofitClient() }
          single { get<Retrofit>().create(PlacesService::class.java) }
          single { LocationServices.getFusedLocationProviderClient(this@MainApplication) }
          singleOf(::LocationUseCase)
          singleOf(::FindRestaurantsUseCase)
          single { params -> this@MainApplication.getSharedPreferences(params.get(), MODE_PRIVATE) }
          single { DisplayPreferencesManager(get(parameters = { parametersOf(PREF_NAME_DISPLAY_PREFS) })) }
          single { FavoritesManager(get(parameters = { parametersOf(PREF_NAME_FAVORITES_MANAGER) })) }
        }
      )
    }
  }
}