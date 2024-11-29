// BancoViewModel.kt
package com.example.bank.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bank.database.AppDatabase
import com.example.bank.database.repository.BancoRepository
import com.example.bank.model.ApiBanco
import com.example.bank.model.Banco
import kotlinx.coroutines.launch
import android.content.Context
import android.content.Intent

class BancoViewModel(application: Application) : AndroidViewModel(application) {
    private val bancoRepository: BancoRepository
    val bancosFromApi = mutableStateOf<List<ApiBanco>?>(null)
    val bancoEncontrado = mutableStateOf<Banco?>(null)

    init {
        val bancoDAO = AppDatabase.getDatabase(application).bancoDAO()
        bancoRepository = BancoRepository(bancoDAO)
    }

    // Consumir a API e salvar os dados no banco Room
    fun fetchAndSaveBancosFromApi() {
        viewModelScope.launch {
            try {
                val bancos = RetrofitInstance.api.getBancos()
                bancosFromApi.value = bancos

                // Mapear os dados da API para entidades do Room
                val bancosRoom = bancos.map { apiBanco ->
                    Banco(
                        nome = apiBanco.fullName,
                        codigo = apiBanco.code?.toString() ?: "N/A"
                    )
                }
                // Salvar os bancos no Room
                bancosRoom.forEach { banco ->
                    bancoRepository.insertBanco(banco)
                }
            } catch (e: Exception) {
                // Lidar com erros de conexão ou API
                e.printStackTrace()
            }
        }
    }

    // Buscar banco por nome no Room
    fun searchBancoByNameAsync(nome: String) {
        viewModelScope.launch {
            bancoEncontrado.value = bancoRepository.findBancoByName(nome)
        }

        fun shareBanco(context: Context, banco: Banco) {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Detalhes do Banco")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Confira as informações do banco:\n\nNome: ${banco.nome}\nCódigo: ${banco.codigo}"
                )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
        }
    }
    fun shareBancosApi(context: Context, bancos: List<ApiBanco>) {
        val shareContent = bancos.joinToString(separator = "\n") { banco ->
            "Nome: ${banco.fullName}, Código: ${banco.code ?: "N/A"}"
        }
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Lista de Bancos")
            putExtra(Intent.EXTRA_TEXT, "Confira a lista de bancos:\n\n$shareContent")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
    }
}

