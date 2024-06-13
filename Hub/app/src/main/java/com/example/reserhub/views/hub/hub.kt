package com.example.reserhub.views.hub

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reserhub.DataBaseController
import com.example.reserhub.R
import com.example.reserhub.entities.types.ServiceDataImpl

class hub : ComponentActivity() {



    private fun insertOnLayout(service: ServiceDataImpl, layout: ScrollView) {
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
        var newLayout = LinearLayout(this).apply{
            orientation = LinearLayout.VERTICAL
            addView(imageView)
            addView(horLayout)
            val layoutParams = LinearLayout.LayoutParams(
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

            setBackgroundColor(ContextCompat.getColor(this@hub, R.color.purple_500)) // Color del divisor
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
        findViewById<LinearLayout>(R.id.linerServiceLayout).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            addView(newLayout)
            addView(space1)
            addView(horizontalDiv)
            addView(space2)
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hub)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = DataBaseController(this)
        val services = db.getAllServices()

        val layout = findViewById<ScrollView>(R.id.servicesLayout)

        for(service in services){
            insertOnLayout(service,layout)
        }

        val setBtn = findViewById<ImageView>(R.id.setBtn)

        setBtn.setOnClickListener{
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.settings_dialog)
            val extiBtn = dialog.findViewById<ImageView>(R.id.exitBtn)
            extiBtn.setOnClickListener{
                dialog.cancel()
            }

            val profileBtn = dialog.findViewById<LinearLayout>(R.id.serviceOptContainer)
            profileBtn.setOnClickListener{
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            }
            dialog.show()
        }

        val scrollUpBtn = findViewById<ImageView>(R.id.scrollUpBtn)

        scrollUpBtn.setOnClickListener{
            layout.smoothScrollTo(0,0)
        }
    }
}