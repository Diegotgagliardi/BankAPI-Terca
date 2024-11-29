package com.example.bank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bank.model.ApiBanco
import com.example.bank.viewmodel.BancoViewModel
import com.example.bank.viewmodel.UserViewModel
import com.example.bank.model.User
import com.example.bank.utils.shareBanco


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by rememberSaveable { mutableStateOf("login") } // Preserva a tela atual após rotação
    val userViewModel: UserViewModel = viewModel()
    val bancoViewModel: BancoViewModel = viewModel()

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = { currentScreen = "search" },
            onRegisterClicked = { currentScreen = "register" }
        )
        "register" -> RegisterScreen(
            onRegisterSuccess = { currentScreen = "login" },
            userViewModel = userViewModel
        )
        "search" -> BankSearchScreen(bancoViewModel = bancoViewModel)
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClicked: () -> Unit
) {
    // Preserve os campos de entrada durante a rotação
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Bem vindo ao AppBank!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onLoginSuccess() }, modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRegisterClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Cadastrar-se")
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    userViewModel: UserViewModel
) {
    // Preserve os campos de entrada durante a rotação
    var name by rememberSaveable { mutableStateOf("") }
    var cpf by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Cadastro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = cpf,
            onValueChange = { cpf = it },
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            userViewModel.addUser(User(cpf = cpf, email = email, nome = name))
            onRegisterSuccess()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cadastrar")
        }
    }
}

@Composable
fun BankSearchScreen(bancoViewModel: BancoViewModel) {
    // Preserve o campo de pesquisa
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val bancosFromApi = bancoViewModel.bancosFromApi.value
    val bancoEncontrado = bancoViewModel.bancoEncontrado.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Busque pelo Banco", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Digite o nome do Banco") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { bancoViewModel.fetchAndSaveBancosFromApi() }, modifier = Modifier.fillMaxWidth()) {
            Text("Lista Bancos (API)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { bancoViewModel.searchBancoByNameAsync(searchQuery) }, modifier = Modifier.fillMaxWidth()) {
            Text("Pesquisar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        bancosFromApi?.let {
            Button(
                onClick = {
                    bancoViewModel.shareBancosApi(context, it)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartilhar Bancos")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        bancosFromApi?.let {
            Text("Bancos atualizados da API:")
            it.forEach { apiBanco ->
                Text("- ${apiBanco.fullName} (Código: ${apiBanco.code ?: "N/A"})")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (bancoEncontrado != null) {
            Text("Banco encontrado no banco de dados:")
            Text("- Nome: ${bancoEncontrado.nome}, Código: ${bancoEncontrado.codigo}")
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    shareBanco(context, bancoEncontrado)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartilhar Banco")
            }
        } else if (searchQuery.isNotEmpty()) {
            Text("Nenhum banco encontrado com o nome \"$searchQuery\".")
        }
    }
}
