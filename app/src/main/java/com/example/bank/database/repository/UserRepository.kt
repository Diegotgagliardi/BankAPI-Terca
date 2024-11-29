package com.example.bank.database.repository

import com.example.bank.dao.UserDAO
import com.example.bank.model.User

class UserRepository(private val userDAO: UserDAO) {

    suspend fun insertUser(user: User) {
        userDAO.insert(user)
    }

    suspend fun getAllUsers(): List<User> {
        return userDAO.getAllUsers()
    }

    suspend fun updateUser(user: User) {
        userDAO.update(user)
    }

    suspend fun deleteUser(user: User) {
        userDAO.delete(user)
    }
}
