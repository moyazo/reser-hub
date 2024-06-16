package com.example.reserhub

class CheckRestrictions {

    // Función para validar el correo electrónico
    fun checkEmail(email: String): Boolean {
        // Expresión regular para un correo electrónico básico
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    // Función para validar la contraseña
    fun checkPassword(password: String): Boolean {
        // Al menos 8 caracteres, una mayúscula, una minúscula y un número
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"
        return password.matches(passwordRegex.toRegex())
    }
}