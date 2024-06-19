package com.example.reserhub.views.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reserhub.DataBaseController
import com.example.reserhub.R
import com.example.reserhub.entities.types.CategoryDataImpl
import com.example.reserhub.entities.types.ServiceDataImpl
import com.example.reserhub.entities.types.UserDataImpl
import com.example.reserhub.views.hub.Hub

class ProfileClient : ComponentActivity() {
    data class EditProfileType(var name:String,var email:String,var password:String)

    private fun  insertCatInDialog(category: CategoryDataImpl, linearDialogLayout:LinearLayout) {
        val textCatView = TextView(this).apply {
            id = category.id!!
            text = category.name
            textSize = 18f
            setTextColor(resources.getColor(android.R.color.black, null))
            gravity = Gravity.CENTER
            setPadding(0,16,0,26)
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }
        textCatView.layoutParams = layoutParams
        linearDialogLayout.addView(textCatView)
    }

    @SuppressLint("SetTextI18n")
    private fun insertOnLayout(service: ServiceDataImpl, layout: HorizontalScrollView) {

        // Crear ImageView y establecer propiedades
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.sevillaartardecer)
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
            )
        }

        // Crear LinearLayout horizontal
        val horLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 0)
        }


        // Crear TextViews y establecer propiedades
        val titleText = TextView(this).apply {
            text = service.title
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f // Peso para distribuir el espacio horizontalmente
            )
        }
        val regex = Regex("""(\d{4}-\d{2}-\d{2})""")

        val startDateMatch = regex.find("${service.startDate}")
        val endDateMatch = regex.find("${service.endDate}")
        val dateText = TextView(this).apply {
            text = "${startDateMatch?.value} / ${endDateMatch?.value}"
            textSize = 11f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f // Peso para distribuir el espacio horizontalmente
            )
        }
        val db = DataBaseController(this)
        val category = db.getCategory(service.categoryId)
        val categoryText = TextView(this).apply {
            text = "${category.name}"
            textSize = 11f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f // Peso para distribuir el espacio horizontalmente
            )
        }

        // Añadir TextViews al layout horizontal
        horLayout.apply {
            addView(titleText)
            addView(dateText)
            addView(categoryText)
        }
        val newLayout = LinearLayout(this).apply{
            orientation = LinearLayout.VERTICAL
            addView(imageView)
            addView(horLayout)
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }
        val horizontalDiv = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                5 // Altura del divisor
            )

            setBackgroundColor(ContextCompat.getColor(this@ProfileClient, R.color.purple_500)) // Color del divisor
        }
        val space1 = Space(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50
            )
        }
        val space2 = Space(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50
            )
        }



        // Crear LinearLayout vertical para contener ImageView y el layout horizontal
        findViewById<LinearLayout>(R.id.linerHorizontalServiceLayout).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
            addView(newLayout)
            addView(space1)
            addView(horizontalDiv)
            addView(space2)
        }
    }

    private fun confirmEditData(newData:EditProfileType) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_profile_edit)
        val editBtn = dialog.findViewById<Button>(R.id.createBtnAction)
        val cancelBtn = dialog.findViewById<Button>(R.id.deleteBtnAction)
        editBtn.setOnClickListener{
            val db =DataBaseController(this)
            val userId = intent.getStringExtra("USER_ID")!!
            val user = UserDataImpl(0,newData.name,newData.email,newData.password,null,null,null,null)
            db.modifyUser(userId,user)
            dialog.cancel()
            recreate()
        }
        cancelBtn.setOnClickListener{
            dialog.cancel()
        }
        dialog.show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_client)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editDataBtn = findViewById<Button>(R.id.editDataBtn)
        editDataBtn.setOnClickListener{
            val nameInput = findViewById<EditText>(R.id.nameInput).text.toString()
            val emailInput = findViewById<EditText>(R.id.emailInput).text.toString()
            val passInput = findViewById<EditText>(R.id.passwordInput).text.toString()
            val newData = EditProfileType(nameInput, emailInput, passInput)
            confirmEditData(newData)
        }
        var catFilter: Int? = null
        var browserFilter: String? = null
        val browser = findViewById<EditText>(R.id.browserHeaderProfile)


        val db = DataBaseController(this)
        val userId = intent.getStringExtra("USER_ID")

        val layout = findViewById<HorizontalScrollView>(R.id.servicesLayoutHorizontal)

        fun loadServices() {
            val services = db.getAllReservaOfUser(userId!!)
            findViewById<LinearLayout>(R.id.linerHorizontalServiceLayout).apply {
                removeAllViewsInLayout()
            }

            for(service in services) {
                insertOnLayout(service, layout)
            }
        }
        loadServices()

        browser.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("browser","test")
                val query = browser.text.toString()
                browserFilter = query
                loadServices()
                true // Indica que la acción ha sido manejada
            } else {
                false
            }
        })

        val clearBtn = findViewById<ImageView>(R.id.clearFilters)
        clearBtn.setOnClickListener{
            catFilter = null
            browserFilter = null
            loadServices()
        }

        val setBtn = findViewById<ImageView>(R.id.setBtn)

        setBtn.setOnClickListener{
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.settings_dialog_for_hub)
            val extiBtn = dialog.findViewById<ImageView>(R.id.exitBtn)
            extiBtn.setOnClickListener{
                dialog.cancel()
            }
            val profileBtn = dialog.findViewById<LinearLayout>(R.id.serviceOptContainerHub)
            profileBtn.setOnClickListener{
                    val intent = Intent(this, Hub::class.java)
                    startActivity(intent)
            }
            dialog.show()
        }

        val scrollUpBtn = findViewById<ImageView>(R.id.scrollUpBtn)

        scrollUpBtn.setOnClickListener{
            layout.smoothScrollTo(0,0)
        }

        val categoryDialog = findViewById<TextView>(R.id.catgeoriesTextHeader)
        categoryDialog.setOnClickListener{
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.change_catgeory_dialog)

            val categories = db.getAllCategories()
            val linearDialogLayout = dialog.findViewById<LinearLayout>(R.id.linearDialogLayout)
            for(category in categories){
                insertCatInDialog(category,linearDialogLayout)
                val cat = dialog.findViewById<TextView>(category.id!!)
                cat.setOnClickListener{
                    catFilter = cat.id
                    loadServices()
                    dialog.cancel()
                }
            }

            dialog.show()
        }
    }
}