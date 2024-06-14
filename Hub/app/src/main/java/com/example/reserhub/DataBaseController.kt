package com.example.reserhub

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.reserhub.entities.Controller
import com.example.reserhub.entities.types.CategoryDataImpl
import com.example.reserhub.entities.types.ReservaDataImpl
import com.example.reserhub.entities.types.ResponseIn
import com.example.reserhub.entities.types.ResponseUp
import com.example.reserhub.entities.types.ServiceDataImpl
import com.example.reserhub.entities.types.SubCategoryDataImpl
import com.example.reserhub.entities.types.UserDataImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                            userId INTEGER NOT NULL,
                            CONSTRAINT FK_category_Services FOREIGN KEY (categoryId) REFERENCES categories(id),
                            CONSTRAINT FK_Company_Services FOREIGN KEY (userId) REFERENCES users(id)
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



         fun insertFakeData() {
             insertFakeUsers(10) // Inserta 10 usuarios ficticios
             insertFakeCategories(5) // Inserta 5 categorías ficticias
             insertFakeSubCategories(10) // Inserta 10 subcategorías ficticias
             insertFakeServices(10) // Inserta 10 servicios ficticios
             // Puedes seguir insertando datos ficticios para otras tablas aquí
         }


         private fun insertFakeUsers(count: Int) {
             val db = writableDatabase
             val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
             val currentDate = sdf.format(Date())

             repeat(count) { index ->
                 val userValues = ContentValues().apply {
                     put("name", "User${index + 1}")
                     put("email", "user${index + 1}@example.com")
                     put("password", "password${index + 1}")
                     put("userName", "username${index + 1}")
                     put("rol", if (index == 0) "ADMIN" else if (index % 2 == 0) "CLIENT" else "COMPANY")
                     put("createdAt", currentDate)
                     put("updatedAt", currentDate)
                 }
                 db.insert("users", null, userValues)
             }
         }

         private fun insertFakeCategories(count: Int) {
             val db = writableDatabase
             val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
             val currentDate = sdf.format(Date())

             repeat(count) { index ->
                 val categoryValues = ContentValues().apply {
                     put("name", "Category${index + 1}")
                     put("createdAt", currentDate)
                     put("updatedAt", currentDate)
                 }
                 db.insert("categories", null, categoryValues)
             }
         }

         private fun insertFakeSubCategories(count: Int) {
             val db = writableDatabase
             val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
             val currentDate = sdf.format(Date())

             repeat(count) { index ->
                 val subCategoryValues = ContentValues().apply {
                     put("name", "SubCategory${index + 1}")
                     put("createdAt", currentDate)
                     put("updatedAt", currentDate)
                     put("categoryId", (index % 5) + 1) // Asigna categorías de forma cíclica
                 }
                 db.insert("subCategories", null, subCategoryValues)
             }
         }

         private fun insertFakeServices(count: Int) {
             val db = writableDatabase
             val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
             val currentDate = sdf.format(Date())

             repeat(count) { index ->
                 val serviceValues = ContentValues().apply {
                     put("title", "Service${index + 1}")
                     put("description", "Description of Service${index + 1}")
                     put("startDate", currentDate)
                     put("endDate", currentDate)
                     put("createdAt", currentDate)
                     put("updatedAt", currentDate)
                     put("categoryId", (index % 5) + 1) // Asigna categorías de forma cíclica
                     put("userId", ((index % 10) + 1) + if (index < 5) 0 else 5) // Asigna usuarios de forma cíclica
                 }
                 db.insert("services", null, serviceValues)
             }
         }







         override fun signUp(email: String, name: String, password: String): ResponseUp {

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

             if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                 return ResponseUp(false, "Must fill all fields",userNull)
             }
             val checker = CheckRestrictions()

             val emailCheck = checker.checkEmail(email)
             val passCheck = checker.checkPassword(password)
             if (!emailCheck && !passCheck) {
                 return ResponseUp(false, "Invalid",userNull)
             } else if(!emailCheck){
                 return ResponseUp(false, "Email invalid",userNull)
             } else if(!passCheck) {
                 return ResponseUp(false, "Al menos 8 caracteres, una mayúscula, una minúscula y un número",userNull)
             }

             val db = writableDatabase
             val getUsersQuery = """SELECT * FROM users WHERE email = ?"""
             val cursor = db.rawQuery(getUsersQuery, arrayOf(email))

             if (cursor.moveToFirst()) {
                 cursor.close()
                 return ResponseUp(
                     false,
                     "User already exists",
                     userNull
                 ) // El usuario ya existe en la base de datos
             } else {
                 val newValues = ContentValues().apply {
                     put("name", name)
                     put("email", email)
                     put("password", password)
                 }
                 db.insert("users", null, newValues)
                 val user: UserDataImpl = getUserByEmail(email)
                 db.close()
                 return ResponseUp(true, "User created successfully",user)
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
             val getUsersQuery = """ SELECT * FROM users WHERE email = ?"""
             val cursor = db.rawQuery(getUsersQuery, arrayOf(email))

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
             val db = writableDatabase
             val getUsersQuery = "SELECT * FROM users"
             val cursor = db.rawQuery(getUsersQuery, null)
             val userList = mutableListOf<UserDataImpl>()

             while (cursor.moveToNext()) {
                 val userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val userName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                 val userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                 val userPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                 val userUserName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
                 val userRol = cursor.getString(cursor.getColumnIndexOrThrow("rol"))
                 val userCreatedAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                 val userUpdatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))


                 // Crea un objeto UserDataImpl con los datos obtenidos
                 val user = UserDataImpl(userId, userName, userEmail,userPassword,userUserName,userRol,userCreatedAt,userUpdatedAt)
                 userList.add(user)
             }

             cursor.close()
             return userList
         }


         override fun getUserById(id: Int): UserDataImpl {
             val db = writableDatabase
             val getUsersQuery = "SELECT * FROM users WHERE id = ?"
             val cursor = db.rawQuery(getUsersQuery, null)

             val userNull = UserDataImpl(null, null, null,null,null,null,null,null)

             if (cursor.moveToFirst()) {
                 val userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val userName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                 val userEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                 val userPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                 val userUserName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
                 val userRol = cursor.getString(cursor.getColumnIndexOrThrow("rol"))
                 val userCreatedAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                 val userUpdatedAt = cursor.getString(cursor.getColumnIndexOrThrow("updatedAt"))

                 val user = UserDataImpl(userId, userName, userEmail,userPassword,userUserName,userRol,userCreatedAt,userUpdatedAt)
                 cursor.close()
                 return user
             }

            return userNull
         }

         override fun getUserByEmail(email: String?): UserDataImpl {
             val db = writableDatabase
             val getUsersQuery = """ SELECT * FROM users WHERE email = ?"""
             val cursor = db.rawQuery(getUsersQuery, arrayOf(email))

             if(cursor.moveToFirst()){
                 val id = cursor.getInt(0)
                 val name = cursor.getString(1)
                 val email = cursor.getString(2)
                 val password = cursor.getString(3)
                 val userName = cursor.getString(4)
                 val rol = cursor.getString(5)
                 val createdAt = cursor.getString(6)
                 val updatedAt = cursor.getString(7)

                 val userFound = UserDataImpl(id,name,email,password,userName,rol,createdAt,updatedAt)
                 cursor.close()
                 return userFound
             } else {
                 val userFound = UserDataImpl(null,null,null,null,null,null,null,null)
                 cursor.close()
                 return userFound
             }
         }

         override fun createUser(newData: UserDataImpl): Boolean {
             val db = writableDatabase
             val getUsersQuery = "SELECT * FROM users WHERE id = ?"
             val cursor = db.rawQuery(getUsersQuery, null)

             if(cursor.moveToFirst()) {
                 cursor.close()
                 return false
             } else {
                 val newValues = ContentValues().apply {
                     put("name", newData.name)
                     put("email", newData.email)
                     put("password", newData.password)
                     put("userName", newData.userName)
                     put("rol", newData.rol)
                     put("createdAt", newData.createdAt)
                     put("updatedAt", newData.updatedAt)
                 }
                 db.insert("users", null, newValues)
                 val user = getUserByEmail(newData.email)
                 cursor.close()
                 db.close()
                 return user.id != null
             }
         }

         override fun modifyUser(id: Int?, newData: UserDataImpl): Boolean {
             val db = writableDatabase

             // Consulta para encontrar el usuario con el ID dado
             val getUserQuery = "SELECT * FROM users WHERE id = ?"
             val cursor = db.rawQuery(getUserQuery, arrayOf(id.toString()))

             // Verifica si el cursor tiene algún resultado
             if (cursor.moveToFirst()) {
                 // Crea un objeto ContentValues con los nuevos valores
                 val newValues = ContentValues().apply {
                     put("name", newData.name)
                     put("email", newData.email)
                     put("password", newData.password)
                     put("userName", newData.userName)
                     put("rol", newData.rol)
                     put("createdAt", newData.createdAt)
                     put("updatedAt", newData.updatedAt)
                 }

                 // Actualiza el usuario con los nuevos valores
                 val rowsAffected = db.update("users", newValues, "id = ?", arrayOf(id.toString()))

                 cursor.close()

                 // Devuelve true si al menos una fila fue afectada por la actualización
                 return rowsAffected > 0
             } else {
                 // Si no se encuentra el usuario, cierra el cursor y devuelve false
                 cursor.close()
                 return false
             }
         }


         override fun deleteUser(id: Int?): Boolean {
             val db = writableDatabase

             // Elimina el usuario con el ID dado
             val rowsAffected = db.delete("users", "id = ?", arrayOf(id.toString()))

             // Devuelve true si al menos una fila fue afectada por la eliminación
             return rowsAffected > 0
         }


         override fun getAllCategories(): List<CategoryDataImpl> {
             val db = writableDatabase
             val getCategoriesQuery = "SELECT * FROM categories"
             val cursor = db.rawQuery(getCategoriesQuery, null)
             val categoriesList = mutableListOf<CategoryDataImpl>()

             while (cursor.moveToNext()) {
                 val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                 // Crea un objeto UserDataImpl con los datos obtenidos
                 val category = CategoryDataImpl(categoryId, categoryName)

                 categoriesList.add(category)
             }

             cursor.close()
             return categoriesList
         }

         override fun getCategory(id: Int): CategoryDataImpl {
             val db = writableDatabase
             val getCategoryQuery = """SELECT * FROM categories WHERE id = ?"""
             val cursor = db.rawQuery(getCategoryQuery, arrayOf("$id"))

             val categoryNull = CategoryDataImpl(null, null)

             if (cursor.moveToFirst()) {
                 val categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                 val category = CategoryDataImpl(categoryId, categoryName)
                 cursor.close()
                 db.close()
                 return category
             }
             cursor.close()
             db.close()
             return categoryNull
         }

         override fun createCategory(newDataImpl: CategoryDataImpl): Boolean {
             val db = writableDatabase
             val categories =  getAllCategories()

             for(category in categories) {
                 if(category.name == newDataImpl.name){
                     return false
                 }
             }
             val newValues = ContentValues().apply {
                 put("name", newDataImpl.name)
             }
             val newId = db.insert("categories", null, newValues)
             val id = newId.toInt()
             val category = getCategory(id)
             db.close()
             return category.name != null
         }

         override fun modifyCategory(id: Int, newData: CategoryDataImpl): Boolean {
             val db = writableDatabase
             val getCategoryQuery  = "SELECT * FROM categories WHERE id = ? "
             val cursor = db.rawQuery(getCategoryQuery, null)

             if (cursor.moveToFirst()) {
                 // Crea un objeto ContentValues con los nuevos valores
                 val newValues = ContentValues().apply {
                     put("name", newData.name)
                 }

                 // Actualiza el usuario con los nuevos valores
                 val rowsAffected = db.update("users", newValues, "id = ?", arrayOf(id.toString()))

                 cursor.close()

                 // Devuelve true si al menos una fila fue afectada por la actualización
                 return rowsAffected > 0
             } else {
                 // Si no se encuentra el usuario, cierra el cursor y devuelve false
                 cursor.close()
                 return false
             }
         }

         override fun deleteCategory(id: Int): Boolean {
             val db = writableDatabase

             // Elimina el usuario con el ID dado
             val rowsAffected = db.delete("categories", "id = ?", arrayOf(id.toString()))

             // Devuelve true si al menos una fila fue afectada por la eliminación
             return rowsAffected > 0
         }

         override fun getAllServices(catFilter: Int?,browserFilter: String?): List<ServiceDataImpl> {
             val db = writableDatabase
             var getServicesQuery = "SELECT * FROM services"
             var selectionArgs: Array<String>? = null

             if (catFilter != null) {
                 getServicesQuery = "SELECT * FROM services WHERE categoryId = ?"
                 selectionArgs = arrayOf(catFilter.toString())
             }

             if (!browserFilter.isNullOrBlank()) {
                 if (selectionArgs == null) {
                     getServicesQuery += " WHERE title LIKE ?"
                     selectionArgs = arrayOf("%$browserFilter%")
                 } else {
                     getServicesQuery += " AND title LIKE ?"
                     selectionArgs += arrayOf("%$browserFilter%")
                 }
             }

             val cursor = db.rawQuery(getServicesQuery, selectionArgs)
             val serviceList = mutableListOf<ServiceDataImpl>()

             while (cursor.moveToNext()) {
                 val serviceId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val serviceTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                 val serviceDes = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                 val serviceStartDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"))
                 val serviceEndDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"))
                 val serviceCategryId = cursor.getString(cursor.getColumnIndexOrThrow("categoryId"))
                 val catIdToInt = serviceCategryId.toInt()
                 val serviceUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"))
                 val userIdToInt = serviceUserId.toInt()

                 // Crea un objeto UserDataImpl con los datos obtenidos
                 val user = ServiceDataImpl(serviceId,serviceTitle,serviceDes,serviceStartDate,serviceEndDate,catIdToInt,userIdToInt)
                 serviceList.add(user)
             }

             cursor.close()
             return serviceList
         }

         override fun getService(id: Int): ServiceDataImpl {
             TODO("Not yet implemented")
         }

         override fun getServicesByUser(userId: Int?): List<ServiceDataImpl> {
             val db = writableDatabase
             val getServicesQuery = "SELECT * FROM services WHERE userId = ?"
             val cursor = db.rawQuery(getServicesQuery, arrayOf("$userId"))
             val servicesList = mutableListOf<ServiceDataImpl>()

             while (cursor.moveToNext()) {
                 val serviceId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val serviceName = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                 val serviceDescription = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                 val serviceStartDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"))
                 val serviceEndDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"))
                 val serviceCategoryId = cursor.getString(cursor.getColumnIndexOrThrow("categoryId"))
                 val serviceToInt = serviceCategoryId.toInt()
                 val serviceUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"))
                 val userToInt = serviceUserId.toInt()
                 // Aquí obtienes los demás datos del servicio según sea necesario

                 // Crea un objeto ServiceDataImpl con los datos obtenidos
                 val service = ServiceDataImpl(serviceId, serviceName, serviceDescription, serviceStartDate, serviceEndDate, serviceToInt,userToInt)
                 servicesList.add(service)
             }

             cursor.close()
             return servicesList
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

         override fun getAllSubCategories(): MutableList<SubCategoryDataImpl> {
             val db = writableDatabase
             val getSubCategoriesQuery = "SELECT * FROM subCategories"
             val cursor = db.rawQuery(getSubCategoriesQuery, null)
             val subCategoriesList = mutableListOf<SubCategoryDataImpl>()

             while (cursor.moveToNext()) {
                 val subCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                 val subCategoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                 val subCategoryCategoryId = cursor.getString(cursor.getColumnIndexOrThrow("categoryId"))
                 val catIdToInt = subCategoryCategoryId.toInt()
                 // Crea un objeto UserDataImpl con los datos obtenidos
                 val subCategory = SubCategoryDataImpl(subCategoryId, subCategoryName,catIdToInt)

                 subCategoriesList.add(subCategory)
             }

             cursor.close()
             return subCategoriesList
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