package com.example.devclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// 1. CORREÇÃO DA IMPORTAÇÃO (deve ter 'N' maiúsculo)
import com.example.devclicker.navigation.AppNavigation
import com.example.devclicker.ui.theme.DevClickerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevClickerTheme {
            
                AppNavigation()
            }
        }
    }
}