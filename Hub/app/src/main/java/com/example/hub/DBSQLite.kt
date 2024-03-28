package com.example.hub

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// : es como extends en Java
class DBSQLite (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla "users"
        val createTableQuery = """
            CREATE TABLE users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar la tabla "clientes" si existe y volver a crearla
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }
    data class SignUpResult(val message: String, val isSuccess: Boolean)
    fun signUp (userData: User): SignUpResult {
        val db = writableDatabase
        val getUsersQuery = """SELECT * FROM users WHERE email = ?"""
        val cursor = db.rawQuery(getUsersQuery, arrayOf(userData.email))

        if (cursor.moveToFirst()) {
            cursor.close()
            return SignUpResult("Ya existe un usuario con el email ${userData.email}", isSuccess = false) // El usuario ya existe en la base de datos
        } else {
            if(userData.name.isEmpty() || userData.email.isEmpty() || userData.password.isEmpty()) {
                return SignUpResult("Debes rellenar todos los campos", isSuccess = false)
            }
            val newValues = ContentValues().apply {
                put("name", userData.name)
                put("email", userData.email)
                put("password", userData.password)
            }
            db.insert("users", null, newValues)
            db.close()
            return SignUpResult("Todo Ok", isSuccess = true)
        }
    }
    data class SignInResult(val user: User?, val isSuccess: Boolean)
    @SuppressLint("Range")
    fun signIn (email: String, pass: String): SignInResult {
        val db = writableDatabase
        val getUsersQuery = """SELECT * FROM users WHERE email = ? AND password = ?"""
        val cursor = db.rawQuery(getUsersQuery, arrayOf(email,pass))
        var user: User? = null

        if (cursor.moveToFirst()) {
            if(email.isEmpty() || pass.isEmpty()) {
                return SignInResult(user, isSuccess = false)
            }
            val userId = cursor.getInt(cursor.getColumnIndex("id"))
            val userName = cursor.getString(cursor.getColumnIndex("name"))
            val userEmail = cursor.getString(cursor.getColumnIndex("email"))
            val userPassword = cursor.getString(cursor.getColumnIndex("password"))

            user = User(userId,userName, userEmail, userPassword)
            cursor.close()
            return SignInResult(user, isSuccess = true) // Ha encontrado el user
        } else {
            cursor.close()
           return SignInResult(user, isSuccess = false)
        }

    }

    fun createUserFun(userData: User): Long {
        val db: SQLiteDatabase  = writableDatabase
        val newValues: ContentValues = ContentValues().apply{
            put("name",userData.name)
            put("email",userData.email)
            put("password",userData.password)
        }

        val newUser = db.insert("users",null,newValues)
        db.close()

        return newUser
    }

}