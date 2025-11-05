package com.example.devclicker.data.remote.dto

// BuyUpgradeResponse.kt
data class BuyUpgradeResponse(
    val success: Boolean,
    val newCoins: Double?,
    val newClickPower: Double?,
    val message: String
)