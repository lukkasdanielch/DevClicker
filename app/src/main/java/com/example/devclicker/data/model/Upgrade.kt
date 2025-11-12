package com.example.devclicker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "upgrades_comprados",
    foreignKeys = [ForeignKey(
        entity = Jogador::class,
        parentColumns = ["id"],
        childColumns = ["jogadorId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class Upgrade(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val upgradeApiId: String,
    val nome: String,
    val preco: Long,
    val efeito: String,
    val jogadorId: Int
)