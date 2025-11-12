package com.example.devclicker.data.model

import androidx.room.ColumnInfo // 1. IMPORTE O ColumnInfo
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
data class UpgradeComprado(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 2. (A CORREÇÃO PRINCIPAL)
    // Mapeia a coluna "upgradeApiId" (do banco)
    // para a propriedade "upgradeId" (do Kotlin)
    @ColumnInfo(name = "upgradeApiId")
    val upgradeId: String,

    val nome: String,

    // 3. (A SEGUNDA CORREÇÃO)
    // Adicione as colunas que estavam faltando
    // (O erro disse que a query retornou 'preco' e 'efeito')
    val preco: Long,
    val efeito: String,

    val jogadorId: Int
)