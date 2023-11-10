package com.alltrails.lunch.app

import android.app.Application
import com.alltrails.lunch.app.viewModel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

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
        }
      )
    }
  }
}