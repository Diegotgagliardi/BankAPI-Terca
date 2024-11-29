// Utils.kt
package com.example.bank.utils

import android.content.Context
import android.content.Intent
import com.example.bank.model.Banco

fun shareBanco(context: Context, banco: Banco) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // Define o tipo de conteúdo compartilhado como texto simples
        putExtra(Intent.EXTRA_SUBJECT, "Detalhes do Banco") // Título do conteúdo
        putExtra(
            Intent.EXTRA_TEXT,
            "Confira as informações do banco:\n\nNome: ${banco.nome}\nCódigo: ${banco.codigo}"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
}
