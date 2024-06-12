package com.example.reserhub.views.dashboard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import com.example.reserhub.DataBaseController
import com.example.reserhub.R
import com.example.reserhub.entities.types.CategoryDataImpl
import com.example.reserhub.entities.types.ServiceDataImpl
import com.example.reserhub.entities.types.SubCategoryDataImpl
import com.example.reserhub.entities.types.UserDataImpl
import java.sql.Date
import java.time.LocalDate

class SuperAdmin : ComponentActivity() {

    fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }



    private fun setEstiloCelda(textoCelda: TextView){
        textoCelda.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        val marginInPixels = dpToPx(20, textoCelda.context)
        params.setMargins(marginInPixels, 0, 0, 0)

        textoCelda.layoutParams = params
        textoCelda.gravity = Gravity.CENTER
    }

    @SuppressLint("ResourceAsColor")
    private fun insertOnUserTable (user: UserDataImpl, tableLayout: TableLayout) {
       try {
           val newRow = TableRow(tableLayout.context).apply {
               setBackgroundResource(R.drawable.new_row)
           }
           val userIdRow = TextView(tableLayout.context).apply {
               text = "${user.id}"
           }
           val userNameRow = TextView(tableLayout.context).apply {
               text = "${user.name}"
           }
           val userRolRow = TextView(tableLayout.context).apply {
               text = "${user.rol}"
           }
           val userEmailRow = TextView(tableLayout.context).apply {
               val regex = Regex("\\w+@")
               val matchResult = user.email?.let { regex.find(it) }
               text = (matchResult?.value + "...") ?: user.email
           }

           val editBtn = ImageView(tableLayout.context).apply {
               setBackgroundResource(R.drawable.baseline_edit)
           }
           val deleteBtn = ImageView(tableLayout.context).apply {
               setBackgroundResource(R.drawable.baseline_delete)
           }
           val newBtn = ImageView(tableLayout.context).apply {
               setBackgroundResource(R.drawable.rounded_tab_new_right_24)
           }
           val db = DataBaseController(this)
           deleteBtn.setOnClickListener {
               try {
                   val dialog = Dialog(this)
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                   dialog.setCancelable(true)
                   dialog.setContentView(R.layout.delete_user_dialog)
                   val createBtnAction = dialog.findViewById<Button>(R.id.createBtnAction)
                   val deleteBtnAction = dialog.findViewById<Button>(R.id.deleteBtnAction)

                   createBtnAction.setOnClickListener{
                       db.deleteUser(user.id)
                       db.close()
                       recreate()
                   }

                   deleteBtnAction.setOnClickListener{
                       dialog.cancel()
                   }


                   dialog.show()
               } catch (error: Exception) {
                   Log.d("Error","$error")
               }
           }

           editBtn.setOnClickListener {
               try {
                   val dialog = Dialog(this)
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                   dialog.setCancelable(true)
                   dialog.setContentView(R.layout.edit_user_dialog)
                   val createBtnAction = dialog.findViewById<Button>(R.id.createBtnAction)
                   val deleteBtnAction = dialog.findViewById<Button>(R.id.deleteBtnAction)

                   createBtnAction.setOnClickListener{

                       val newName = dialog.findViewById<EditText>(R.id.nameInput).text.toString()
                       val newUserName = dialog.findViewById<EditText>(R.id.userNameInput).text.toString()
                       val newUserRol = dialog.findViewById<EditText>(R.id.rolInput).text.toString()
                       val newUserPass = dialog.findViewById<EditText>(R.id.passInput).text.toString()

                       val currentDate = LocalDate.now()

                       val newUser = UserDataImpl(0,newName,user.email,newUserPass,newUserName,newUserRol,"$currentDate","$currentDate")
                       db.modifyUser(user.id,newUser)
                       db.close( )
                       recreate()
                       Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()
                   }

                   deleteBtnAction.setOnClickListener{
                       dialog.cancel()
                   }


                   dialog.show()
               } catch (error: Exception) {
                   Log.d("Error","$error")
               }
           }

           newBtn.setOnClickListener {
               try {
                   val dialog = Dialog(this)
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                   dialog.setCancelable(true)
                   dialog.setContentView(R.layout.new_user_dialog)
                   val createBtnAction = dialog.findViewById<Button>(R.id.createBtnAction)
                   val deleteBtnAction = dialog.findViewById<Button>(R.id.deleteBtnAction)

                   createBtnAction.setOnClickListener{


                       val newUserEmail = dialog.findViewById<EditText>(R.id.EmailInput).text.toString()
                       val newName = dialog.findViewById<EditText>(R.id.nameInput).text.toString()
                       val newUserName = dialog.findViewById<EditText>(R.id.userNameInput).text.toString()
                       val newUserRol = dialog.findViewById<EditText>(R.id.rolInput).text.toString()
                       val newUserPass = dialog.findViewById<EditText>(R.id.passInput).text.toString()

                       val currentDate = LocalDate.now()

                       val newUser = UserDataImpl(0,newName,newUserEmail,newUserPass,newUserName,newUserRol,"$currentDate","$currentDate")
                       db.createUser(newUser)
                       db.close()
                       insertOnUserTable(newUser,tableLayout)
                       recreate()
                       Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()
                   }

                   deleteBtnAction.setOnClickListener{
                       dialog.cancel()
                   }


                   dialog.show()
               } catch (error: Exception) {
                   Log.d("Error","$error")
                   Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
               }
           }

           setEstiloCelda(userIdRow)
           setEstiloCelda(userNameRow)
           setEstiloCelda(userRolRow)
           setEstiloCelda(userEmailRow)

           newRow.addView(userIdRow)
           newRow.addView(userNameRow)
           newRow.addView(userRolRow)
           newRow.addView(userEmailRow)
           newRow.addView(editBtn)
           newRow.addView(deleteBtn)
           newRow.addView(newBtn)


           tableLayout.addView(newRow)
       } catch (error: Exception){
           Log.d("Error", "$error")
       }
    }

    private fun insertOnServiceTable (service: ServiceDataImpl, tableLayout: TableLayout) {
        try {
            val newRow = TableRow(tableLayout.context).apply {
                setBackgroundResource(R.drawable.new_row)
            }

            val editBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_edit)
            }
            val deleteBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_delete)
            }
            val newBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.rounded_tab_new_right_24)
            }

            val serviceIdRow = TextView(tableLayout.context).apply {
                text = "${service.id}"
            }
            val serviceTitleRow = TextView(tableLayout.context).apply {
                text = "${service.title}"
            }
            val serviceCategoryIdRow = TextView(tableLayout.context).apply {
                text = "${service.categoryId}"
            }
            val serviceUserIdRow = TextView(tableLayout.context).apply {
                text = "${service.userId}"
            }

            setEstiloCelda(serviceIdRow)
            setEstiloCelda(serviceTitleRow)
            setEstiloCelda(serviceCategoryIdRow)
            setEstiloCelda(serviceUserIdRow)

            newRow.addView(serviceIdRow)
            newRow.addView(serviceTitleRow)
            newRow.addView(serviceCategoryIdRow)
            newRow.addView(serviceUserIdRow)
            newRow.addView(editBtn)
            newRow.addView(deleteBtn)
            newRow.addView(newBtn)

            tableLayout.addView(newRow)
        } catch (error: Exception){
            Log.d("Error", "$error")
        }
    }
    private fun insertOnCategoryTable (category: CategoryDataImpl, tableLayout: TableLayout) {
        try {
            val newRow = TableRow(tableLayout.context).apply {
                setBackgroundResource(R.drawable.new_row)
            }

            val editBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_edit)
            }
            val deleteBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_delete)
            }
            val newBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.rounded_tab_new_right_24)
            }

            val categoryIdRow = TextView(tableLayout.context).apply {
                text = "${category.id}"
            }
            val categoryName = TextView(tableLayout.context).apply {
                text = "${category.name}"
            }

            setEstiloCelda(categoryIdRow)
            setEstiloCelda(categoryName)

            newRow.addView(categoryIdRow)
            newRow.addView(categoryName)
            newRow.addView(editBtn)
            newRow.addView(deleteBtn)
            newRow.addView(newBtn)

            tableLayout.addView(newRow)
        } catch (error: Exception){
            Log.d("Error", "$error")
        }
    }
    private fun insertOnSubCategoryTable (subCategory: SubCategoryDataImpl, tableLayout: TableLayout) {
        try {
            val newRow = TableRow(tableLayout.context).apply {
                setBackgroundResource(R.drawable.new_row)
            }

            val editBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_edit)
            }
            val deleteBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.baseline_delete)
            }
            val newBtn = ImageView(tableLayout.context).apply {
                setBackgroundResource(R.drawable.rounded_tab_new_right_24)
            }

            val subCategoryIdRow = TextView(tableLayout.context).apply {
                text = "${subCategory.id}"
            }
            val subCategoryNameRow = TextView(tableLayout.context).apply {
                text = "${subCategory.name}"
            }
            val subCategoryCategoryRow = TextView(tableLayout.context).apply {
                text = "${subCategory.categoryId}"
            }

            setEstiloCelda(subCategoryIdRow)
            setEstiloCelda(subCategoryNameRow)
            setEstiloCelda(subCategoryCategoryRow)

            newRow.addView(subCategoryIdRow)
            newRow.addView(subCategoryNameRow)
            newRow.addView(subCategoryCategoryRow)
            newRow.addView(editBtn)
            newRow.addView(deleteBtn)
            newRow.addView(newBtn)

            tableLayout.addView(newRow)
        } catch (error: Exception){
            Log.d("Error", "$error")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_super_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val tablaUser = findViewById<TableLayout>(R.id.UsersContainerTable)
        val tablaService = findViewById<TableLayout>(R.id.ServicesContainerTable)
        val tablaCategory = findViewById<TableLayout>(R.id.CategoriesContainerTable)
        val tablaSubCategory = findViewById<TableLayout>(R.id.SubCategoriesContainerTable)

        val db = DataBaseController(this)
        val users = db.getAllUsers()
        val services = db.getAllServices()
        val categories = db.getAllCategories()
        val subCategories = db.getAllSubCategories()
        for(user in users){
            insertOnUserTable(user, tablaUser)
        }
        for(service in services){
            insertOnServiceTable(service, tablaService)
        }
        for(category in categories){
            insertOnCategoryTable(category, tablaCategory)
        }
        for(subCategory in subCategories){
            insertOnSubCategoryTable(subCategory, tablaSubCategory)
        }

        val menuBtn = findViewById<ImageView>(R.id.MenuBars)

        menuBtn.setOnClickListener{
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.super_admin_menu)
            var exitBtn = dialog.findViewById<ImageView>(R.id.exitBtn)
            exitBtn.setOnClickListener{
                dialog.cancel()
            }

            val userItem = dialog.findViewById<LinearLayout>(R.id.userOptContainer)
            val serviceContainer= findViewById<LinearLayout>(R.id.ServicesContainer)
            val catContainer = findViewById<LinearLayout>(R.id.CategoriesContainer)
            val subCatContainer = findViewById<LinearLayout>(R.id.SubCategoriesContainer)
            val userContainer= findViewById<LinearLayout>(R.id.UsersContainer)
            userItem.setOnClickListener{
                // Muestra u oculta contenedores según el ítem del menú seleccionado
                if (serviceContainer.visibility == View.VISIBLE) {
                    serviceContainer.visibility = View.GONE
                }

                // Opcional: Puedes añadir lógica similar para otros contenedores
                // Por ejemplo, para el contenedor de categorías:
                if (catContainer.visibility == View.VISIBLE) {
                    catContainer.visibility = View.GONE
                }

                // Y para el contenedor de subcategorías:
                if (subCatContainer.visibility == View.VISIBLE) {
                    subCatContainer.visibility = View.GONE
                }
                dialog.cancel()
            }
            val serviceItem = dialog.findViewById<LinearLayout>(R.id.serviceOptContainer)
            serviceItem.setOnClickListener{
                if (serviceContainer.visibility == View.GONE) {
                    serviceContainer.visibility = View.VISIBLE
                }
                // Muestra u oculta contenedores según el ítem del menú seleccionado
                if (userContainer.visibility == View.VISIBLE) {
                    userContainer.visibility = View.GONE
                }

                // Opcional: Puedes añadir lógica similar para otros contenedores
                // Por ejemplo, para el contenedor de categorías:
                if (catContainer.visibility == View.VISIBLE) {
                    catContainer.visibility = View.GONE
                }

                // Y para el contenedor de subcategorías:
                if (subCatContainer.visibility == View.VISIBLE) {
                    subCatContainer.visibility = View.GONE
                }
                dialog.cancel()
            }
            val catItem = dialog.findViewById<LinearLayout>(R.id.catOptContainer)
            catItem.setOnClickListener{
                if (catContainer.visibility == View.GONE) {
                    catContainer.visibility = View.VISIBLE
                }
                // Muestra u oculta contenedores según el ítem del menú seleccionado
                if (serviceContainer.visibility == View.VISIBLE) {
                    serviceContainer.visibility = View.GONE
                } else {
                    serviceContainer.visibility = View.VISIBLE
                }

                // Opcional: Puedes añadir lógica similar para otros contenedores
                // Por ejemplo, para el contenedor de categorías:
                if (userContainer.visibility == View.VISIBLE) {
                    userContainer.visibility = View.GONE
                }

                // Y para el contenedor de subcategorías:
                if (subCatContainer.visibility == View.VISIBLE) {
                    subCatContainer.visibility = View.GONE
                }
                dialog.cancel()
            }
            val subCatItem = dialog.findViewById<LinearLayout>(R.id.subCatOptContainer)
            subCatItem.setOnClickListener{
                if (subCatContainer.visibility == View.GONE) {
                    subCatContainer.visibility = View.VISIBLE
                }
                // Muestra u oculta contenedores según el ítem del menú seleccionado
                if (serviceContainer.visibility == View.VISIBLE) {
                    serviceContainer.visibility = View.GONE
                }

                // Opcional: Puedes añadir lógica similar para otros contenedores
                // Por ejemplo, para el contenedor de categorías:
                if (catContainer.visibility == View.VISIBLE) {
                    catContainer.visibility = View.GONE
                }

                // Y para el contenedor de subcategorías:
                if (userContainer.visibility == View.VISIBLE) {
                    userContainer.visibility = View.GONE
                }
                dialog.cancel()
            }
            dialog.show()
        }

    }
}