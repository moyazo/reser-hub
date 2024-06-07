package com.example.reserhub

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.reserhub.entities.Controller
import com.example.reserhub.entities.types.CategoryDataImpl
import com.example.reserhub.entities.types.ReservaDataImpl
import com.example.reserhub.entities.types.ResponseIn
import com.example.reserhub.entities.types.ResponseUp
import com.example.reserhub.entities.types.ServiceDataImpl
import com.example.reserhub.entities.types.SubCategoryDataImpl
import com.example.reserhub.entities.types.UserDataImpl

class DataBaseController(context: Context): SQLiteOpenHelper (context, DATABASE_NAME , null, DATABASE_VERSION),
    Controller
     {
    companion object{
        private const val DATABASE_NAME  = "reserhub.db"
        private const val DATABASE_VERSION = 1
    }

         override fun onCreate(db: SQLiteDatabase) {
             // Tus consultas de creación de tablas
             val userTableQuery = """
             CREATE TABLE users(
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            email TEXT NOT NULL,
                            password TEXT NOT NULL,
            				userName TEXT ,
            				rol TEXT,
            				createdAt DATE,
                            updatedAt DATE
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

             db.execSQL(userTableQuery)
             db.execSQL(categoryTableQuery)
             db.execSQL(subCategoryTableQuery)
             db.execSQL(servicesTableQuery)
             db.execSQL(reservasTableQuery)
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

         override fun signUp(email: String, name: String, password: String): ResponseUp {

             if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                 return ResponseUp(false, "Must fill all fields")
             }
             val checker = CheckRestrictions();

             val emailCheck = checker.checkEmail(email);
             val passCheck = checker.checkPassword(password);
             if (!emailCheck && !passCheck) {
                 return ResponseUp(false, "Invalid")
             } else if(!emailCheck){
                 return ResponseUp(false, "Email invalid")
             } else if(!passCheck) {
                 return ResponseUp(false, "Al menos 8 caracteres, una mayúscula, una minúscula y un número")
             }

             val db = writableDatabase
             val getUsersQuery = """SELECT * FROM users WHERE email = ?"""
             val cursor = db.rawQuery(getUsersQuery, arrayOf(email))

             if (cursor.moveToFirst()) {
                 cursor.close()
                 return ResponseUp(
                     false,
                     "User already exists"
                 ) // El usuario ya existe en la base de datos
             } else {
                 val newValues = ContentValues().apply {
                     put("name", name)
                     put("email", email)
                     put("password", password)
                 }
                 db.insert("users", null, newValues)
                 db.close()
                 return ResponseUp(true, "User created successfully")
             }
         }


         @SuppressLint("Range")
         override fun logIn(email: String, password: String): ResponseIn {
             val userNull = UserDataImpl(
                 id = null,
                 name = null,
                 email = null,
                 password = null,
                 userName = null,
                 rol = "null",
                 createdAt = null,
                 updatedAt = null
             )

             if(email.isEmpty() || password.isEmpty()){
                 return ResponseIn(status = true, response = "Something is emty", userNull)
             }

             val db = writableDatabase
             val getUsersQuery = """ SELECT * FROM users WHERE email = ?""";
             val cursor = db.rawQuery(getUsersQuery, arrayOf(email));

             if(cursor.moveToFirst()) {
                 val userId = cursor.getInt(cursor.getColumnIndex("id"))
                 val userName = cursor.getString(cursor.getColumnIndex("name"))
                 val userEmail = cursor.getString(cursor.getColumnIndex("email"))
                 val userPassword = cursor.getString(cursor.getColumnIndex("password"))
                 if(userPassword != password) {
                     cursor.close()
                     return ResponseIn(status = false, response = "Wrong credentials", userNull)
                 }
                 val userRol = cursor.getString(cursor.getColumnIndex("rol"))

                 val user = UserDataImpl(
                     id = userId,
                     name = userName,
                     email = userEmail,
                     password = userPassword,
                     userName = userName,
                     rol = userRol,
                     createdAt = null,
                     updatedAt = null
                 )
                 cursor.close()
                 return ResponseIn(status = true, response = "User created succesfully", user)
             }else {
                 cursor.close()
                 return ResponseIn(status = true, response = "User do not found", userNull)
             }
         }
         override fun getAllUsers(): List<UserDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getUserById(id: Int): UserDataImpl {
             TODO("Not yet implemented")
         }

         override fun createUser(newData: UserDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun modifyUser(id: Int, newData: UserDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun deleteUser(id: Int): Boolean {
             TODO("Not yet implemented")
         }

         override fun getAllCategories(): List<CategoryDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getCategory(id: Int): CategoryDataImpl {
             TODO("Not yet implemented")
         }

         override fun createCategory(newDataImpl: UserDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun modifyCategory(id: Int, newData: UserDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun deleteCategory(id: Int): Boolean {
             TODO("Not yet implemented")
         }

         override fun getAllServices(): List<ServiceDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getService(id: Int): ServiceDataImpl {
             TODO("Not yet implemented")
         }

         override fun createService(newDataImpl: ServiceDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun modifyService(id: Int, newData: ServiceDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun deleteService(id: Int): Boolean {
             TODO("Not yet implemented")
         }

         override fun getAllReserva(): List<ReservaDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getAllReservaOfUser(userId: Int): List<ReservaDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getReserva(id: Int, userId: Int): ReservaDataImpl {
             TODO("Not yet implemented")
         }

         override fun createReserva(newDataImpl: ReservaDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun modifyReserva(id: Int, userId: Int, newData: ReservaDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun deleteReserva(id: Int, userId: Int): Boolean {
             TODO("Not yet implemented")
         }

         override fun getAllSubCategories(): List<SubCategoryDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getAllSubCategoriesOfCat(categoryId: Int): List<SubCategoryDataImpl> {
             TODO("Not yet implemented")
         }

         override fun getSubCategory(id: Int): SubCategoryDataImpl {
             TODO("Not yet implemented")
         }

         override fun createSubCategory(newDataImpl: SubCategoryDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun modifySubCategory(id: Int, newData: SubCategoryDataImpl): Boolean {
             TODO("Not yet implemented")
         }

         override fun deleteSubCategory(id: Int): Boolean {
             TODO("Not yet implemented")
         }


     }