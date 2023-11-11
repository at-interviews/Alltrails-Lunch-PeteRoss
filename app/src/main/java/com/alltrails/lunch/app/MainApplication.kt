package com.alltrails.lunch.app

import android.app.Application
import com.alltrails.lunch.app.network.PlacesService
import com.alltrails.lunch.app.network.createRetrofitClient
import com.alltrails.lunch.app.usecase.LocationUpdatesUseCase
import com.alltrails.lunch.app.viewModel.MainViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
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
          viewModelOf(::MainViewModel)
          single(createdAtStart = true) { createRetrofitClient() }
          single { get<Retrofit>().create(PlacesService::class.java) }
          single { LocationServices.getFusedLocationProviderClient(this@MainApplication) }
          singleOf(::LocationUpdatesUseCase)
        }
      )
    }
  }
}