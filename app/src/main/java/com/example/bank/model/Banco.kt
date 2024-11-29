package com.example.bank.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bancos")
data class Banco(
    @PrimaryKey(autoGenerate = true) val idBanco: Int = 0,
    val nome: String,
    val codigo: String
)