package com.example.reserhub

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reserhub.entities.types.CategoryDataImpl
import com.example.reserhub.entities.types.ServiceData

class HubServices : ComponentActivity() {
    private fun setServiceData(textoCelda: TextView){
        textoCelda.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        //textoCelda.gravity = Gravity.CENTER
        textoCelda.setBackgroundResource(R.color.black)
    }
    private fun insertService(service: ServiceData){
        var db = DataBaseController(this)
        var category: CategoryDataImpl  = db.getCategory(service.categoryId)
        var serviceLayout: FrameLayout = FrameLayout(this)
        serviceLayout.setBackgroundResource(R.drawable.service_layout)

        var serviceTitle: TextView = TextView(this)
        serviceTitle.text = service.title
        var serviceDate: TextView = TextView(this)
        serviceDate.text = "${service.startDate} + ${service.endDate}"
        var serviceCategory: TextView = TextView(this)
        serviceCategory.text = category.name
        setServiceData(serviceTitle)
        setServiceData(serviceDate)
        setServiceData(serviceCategory)

        serviceLayout.addView(serviceTitle)
        serviceLayout.addView(serviceDate)
        serviceLayout.addView(serviceCategory)

        val container = findViewById<HorizontalScrollView>(R.id.ServiceScrollView)
        container.addView(serviceLayout)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hub_services)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var db = DataBaseController(this)
        var services = db.getAllServices()
        for (service in services) {
            insertService(service)
        }
    }

}