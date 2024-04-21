package com.example.reserhub.entities

import com.example.reserhub.entities.types.UserDataImpl
import java.sql.Date


interface CustomUser {
    fun signUp(email: String, name: String, password: String) : Boolean
    fun logIn(email: String, password: String) : Boolean

    fun createUser(user: UserDataImpl): Boolean
    fun getAllUsers(): List<UserDataImpl>
    fun getUserById(id: Int): UserDataImpl?
    fun updateUserById(id: Int, user: UserDataImpl): Boolean
    fun deleteUserById(id: Int): Boolean

    // Otros métodos relacionados con usuarios, como cambiar la contraseña, cambiar el nombre de usuario, etc.
}

