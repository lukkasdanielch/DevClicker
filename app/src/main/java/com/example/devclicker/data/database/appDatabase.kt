package com.example.devclicker.data.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.devclicker.data.dao.JogadorDao
import com.example.devclicker.data.dao.UpgradeDao
import com.example.devclicker.data.model.Jogador



@Database(entities = [Jogador::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Dentro da classe AppDatabase
    abstract fun upgradeDao(): UpgradeDao

    abstract fun jogadorDao(): JogadorDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DevClicker"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}