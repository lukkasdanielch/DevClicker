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
    indices = [Index(value = ["jogadorId", "upgradeId"], unique = true)]
)
data class UpgradeComprado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val jogadorId: Int,
    val upgradeId: String,
    val level: Int
)