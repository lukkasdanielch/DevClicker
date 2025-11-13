package com.example.devclicker.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "upgrades_comprados",
    foreignKeys = [ForeignKey(
        entity = Jogador::class,
        parentColumns = ["id"],
        childColumns = ["jogadorId"],
        onDelete = ForeignKey.CASCADE
    )],
    // Precisamos de um índice para encontrar rapidamente o upgrade de um jogador
    indices = [Index(value = ["jogadorId", "upgradeId"], unique = true)]
)
data class UpgradeComprado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val jogadorId: Int,

    // Este é o ID que linka com a definição do upgrade (ex: "click_v1")
    val upgradeId: String,

    // A mudança crucial: Trocamos os campos antigos por "level"
    val level: Int
)