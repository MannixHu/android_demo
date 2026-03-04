package com.example.androidemo

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.androidemo.ui.screens.CounterScreen
import com.example.androidemo.ui.theme.AndroidDemoTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CounterApplication : Application()

@Composable
fun CounterApp() {
    AndroidDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CounterScreen()
        }
    }
}
