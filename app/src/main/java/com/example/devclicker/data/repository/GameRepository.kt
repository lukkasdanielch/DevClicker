package com.example.devclicker.data.repository

import com.example.devclicker.data.database.AppDatabase

// Ele vai receber o AppDatabase (Room)
class GameRepository(private val db: AppDatabase) {

    // (Aqui entrará a lógica de banco de dados, como salvar pontos,
    // buscar upgrades comprados, etc.)

    // Exemplo (você pode adicionar depois):
    // fun getJogador(id: Int) = db.jogadorDao().getJogadorById(id)
}