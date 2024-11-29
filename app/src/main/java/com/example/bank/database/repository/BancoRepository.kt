package com.example.bank.database.repository

import com.example.bank.dao.BancoDAO
import com.example.bank.model.Banco

class BancoRepository(private val bancoDAO: BancoDAO) {

    suspend fun insertBanco(banco: Banco) {
        bancoDAO.insert(banco)
    }

    suspend fun getAllBancos(): List<Banco> {
        return bancoDAO.getAllBancos()
    }

    suspend fun updateBanco(banco: Banco) {
        bancoDAO.update(banco)
    }

    suspend fun deleteBanco(banco: Banco) {
        bancoDAO.delete(banco)
    }

    suspend fun findBancoByName(nome: String): Banco? {
        return bancoDAO.findByName("%$nome%") // Busca parcial
    }

}
