package com.example.reserhub

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.reserhub.entities.CustomUser
import com.example.reserhub.entities.types.UserDataImpl

class DataBaseController(context: Context): SQLiteOpenHelper (context, DATABASENAME, null, DATABASE_VERSION), CustomUser {
    companion object{
        private const val DATABASENAME = "reserhub.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val userTableQuery = """
             CREATE TABLE users(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            email TEXT NOT NULL,
                            password TEXT NOT NULL,
            				userName TEXT NOT NULL,
            				rol TEXT NOT NULL,
            				createdAt DATE NOT NULL,
                            updatedAt DATE NOT NULL
            );
        """.trimIndent()

        val categoryTableQuery = """
            CREATE TABLE categories(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            createdAt DATE NOT NULL,
                            updatedAt DATE NOT NULL
            );
        """.trimIndent()
        val subCategoryTableQuery = """
            CREATE TABLE subCategories(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            createdAt DATE NOT NULL,
                            updatedAt DATE NOT NULL,
                            categoryId INTEGER,
                            CONSTRAINT FK_category_Subcategories FOREIGN KEY (categoryId) REFERENCES categories(id)
            );
        """.trimIndent()
        val servicesTableQuery = """
            CREATE TABLE services(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            title TEXT NOT NULL,
                            description TEXT NOT NULL,
                            startDate DATETIME NOT NULL,
            				endDate DATETIME NOT NULL,
            				createdAt DATE NOT NULL,
                            updatedAt DATE NOT NULL,
                            categoryId INTEGER NOT NULL,
                            CONSTRAINT FK_category_Services FOREIGN KEY (categoryId) REFERENCES categories(id)
            );
        """.trimIndent()
        val reservasTableQuery = """
            CREATE TABLE reservas (
            	id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER NOT NULL,    
                serviceId INTEGER NOT NULL,    
                createdAt DATE NOT NULL,
            	updatedAt DATE NOT NULL,
                CONSTRAINT FK_userId FOREIGN KEY (userId) REFERENCES users(id),
                CONSTRAINT FK_serviceId FOREIGN KEY (serviceId) REFERENCES services(id)
            )
        """.trimIndent()

        db?.execSQL(userTableQuery)
        db?.execSQL(categoryTableQuery)
        db?.execSQL(subCategoryTableQuery)
        db?.execSQL(servicesTableQuery)
        db?.execSQL(reservasTableQuery)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar las tablas si existen y las vuelve a crear
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS categories")
        db.execSQL("DROP TABLE IF EXISTS subCategories")
        db.execSQL("DROP TABLE IF EXISTS services")
        db.execSQL("DROP TABLE IF EXISTS reservas")

        onCreate(db)
    }

    override fun signUp(email: String, name: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun logIn(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun createUser(user: UserDataImpl): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<UserDataImpl> {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Int): UserDataImpl? {
        TODO("Not yet implemented")
    }

    override fun updateUserById(id: Int, user: UserDataImpl): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteUserById(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}