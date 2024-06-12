package com.example.reserhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reserhub.views.login.LoginSignIn
import com.example.reserhub.views.login.LoginSignUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val dbController = DataBaseController(this)
        if(dbController.getAllUsers().isEmpty()){
            dbController.insertFakeData()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val signUpBtn: Button = findViewById<Button>(R.id.signUp)
        // Change activity
        signUpBtn.setOnClickListener{
            val intent = Intent(this, LoginSignUp::class.java)
            startActivity(intent)
        }

        val signInpBtn: Button = findViewById<Button>(R.id.signIn)
        // Change activity
        signInpBtn.setOnClickListener{
            val intent = Intent(this, LoginSignIn::class.java)
            startActivity(intent)
        }
    }
}