package com.example.devclicker.data.remote.dto

/**
 * Requisição enviada ao comprar um upgrade
 */
data class BuyUpgradeRequest(
    val userId: String,
    val upgradeId: String
)
