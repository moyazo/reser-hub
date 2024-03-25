package com.example.hub

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

    fun signUp (userData: User): Boolean {
        val db = writableDatabase
        val getUsersQuery = """SELECT * FROM users WHERE email = ?"""
        val cursor = db.rawQuery(getUsersQuery, arrayOf(userData.email))

        if (cursor.moveToFirst()) {
            cursor.close()
            return false // El usuario ya existe en la base de datos
        } else {
            if(userData.name.isEmpty() || userData.email.isEmpty() || userData.password.isEmpty()) {
                return false
            }
            val newValues = ContentValues().apply {
                put("name", userData.name)
                put("email", userData.email)
                put("password", userData.password)
            }
            db.insert("users", null, newValues)
            db.close()
            return true // Usuario insertado correctamente
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