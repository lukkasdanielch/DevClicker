// UpgradeInfo.kt
package com.example.devclicker.data.remote.dto

data class UpgradeInfo(
    val id: String,
    val name: String,
    val description: String,
    val cost: Double,
    val multiplier: Double
)