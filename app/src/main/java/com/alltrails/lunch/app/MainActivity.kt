package com.alltrails.lunch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alltrails.lunch.app.ui.MainScreen
import com.alltrails.lunch.app.ui.theme.AllTrailsLunchTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      AllTrailsLunchTheme {
        MainScreen()
      }
    }
  }
}
