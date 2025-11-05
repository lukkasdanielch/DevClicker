package com.example.devclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.devclicker.navigation.Appnavigation
import com.example.devclicker.ui.theme.DevClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevClickerTheme {
                Appnavigation()
            }
        }
    }
}
