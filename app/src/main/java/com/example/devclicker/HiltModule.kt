package com.example.devclicker.di

import android.content.Context
import androidx.room.Room
import com.example.devclicker.data.database.AppDatabase
import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao // <-- Importe o UpgradeDao
import com.example.devclicker.data.repository.AuthRepository
import com.example.devclicker.data.repository.GameRepository
import com.example.devclicker.data.repository.JogadorRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    // --- Firebase ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    // --- Room DB ---
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "devclicker_database"
        )   .allowMainThreadQueries()
            .fallbackToDestructiveMigration() // <-- ADICIONE ESTA LINHA
            .build()
    }

    // --- DAOs (As "Ferramentas") ---
    @Provides
    @Singleton
    fun provideJogadorDao(appDatabase: AppDatabase): JogadorDao {
        return appDatabase.jogadorDao()
    }

    @Provides
    @Singleton
    fun provideUpgradeDao(appDatabase: AppDatabase): UpgradeDao {
        return appDatabase.upgradeDao()
    }

    // --- Repositórios (Os "Cérebros") ---
    // (O Hilt já sabe como fazer AuthRepository e JogadorRepository
    // porque eles usam @Inject constructor. Mas para o GameRepository,
    // é bom ser explícito ou garantir que ele também use @Inject)

    // O GameRepository que te passei usa @Inject, então esta
    // função @Provides abaixo NÃO é necessária. O Hilt já sabe como
    // "fabricar" o GameRepository.
}