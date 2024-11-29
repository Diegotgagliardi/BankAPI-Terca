package com.example.bank.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bank.dao.BancoDAO
import com.example.bank.dao.UserDAO
import com.example.bank.model.Banco
import com.example.bank.model.User

@Database(entities = [User::class, Banco::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun bancoDAO(): BancoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(DatabaseCallback()) // Adiciona o Callback aqui
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insere os registros fixos usando SQL
                db.execSQL("INSERT INTO bancos (idBanco, nome, codigo) VALUES (1, 'Banco do Brasil S.A.', '001')")
                db.execSQL("INSERT INTO bancos (idBanco, nome, codigo) VALUES (2, 'Banco Santander (Brasil) S.A.', '033')")
                db.execSQL("INSERT INTO bancos (idBanco, nome, codigo) VALUES (3, 'Caixa Econômica Federal', '104')")
            }
        }
    }
}