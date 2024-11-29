package com.example.bank.database.repository

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bank.database.AppDatabase
import com.example.bank.model.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDAO = AppDatabase.getDatabase(application).userDAO()
    val users = mutableStateOf<List<User>>(listOf())

    init {
        viewModelScope.launch {
            users.value = userDAO.getAllUsers()
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userDAO.insert(user)
            users.value = userDAO.getAllUsers()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userDAO.update(user)
            users.value = userDAO.getAllUsers()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userDAO.delete(user)
            users.value = userDAO.getAllUsers()
        }
    }
}
