package com.example.devclicker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jogadores")
data class Jogador(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val pontos: Long
)