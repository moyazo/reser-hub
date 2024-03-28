package com.example.hub
/**
 * Almacenar BD SQL_lite
 * */
data class User(var id: Int, var name: String, var email:String, var password:String) {
    override fun toString(): String {
        return "$name - $email - $password"
    }

}