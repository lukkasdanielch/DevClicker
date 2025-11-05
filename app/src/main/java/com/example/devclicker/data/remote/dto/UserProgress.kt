// UserProgress.kt
package com.example.devclicker.data.remote.dto

data class UserProgress(
    val userId: String,
    val coins: Double,
    val clickPower: Double,
    val upgradesOwned: List<String>
)