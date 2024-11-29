package com.example.bank.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bank.model.Banco

@Dao
interface BancoDAO {

    @Insert
    suspend fun insert(banco: Banco)

    @Query("SELECT * FROM bancos")
    suspend fun getAllBancos(): List<Banco>

    @Update
    suspend fun update(banco: Banco)

    @Delete
    suspend fun delete(banco: Banco)

    @Query("SELECT * FROM bancos WHERE nome LIKE :nome LIMIT 1")
    suspend fun findByName(nome: String): Banco?
}