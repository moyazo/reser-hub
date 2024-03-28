package com.example.hub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        Toast.makeText(this, "Estás en Sign In", Toast.LENGTH_SHORT).show()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var dbsqLite = DBSQLite(this)

        var signInBtn = findViewById<Button>(R.id.loginBtn)

        signInBtn.setOnClickListener{
            try {
                var emailInput: String = findViewById<EditText>(R.id.emailEditText).text.toString()
                var passInput: String = findViewById<EditText>(R.id.passEditText).text.toString()

                val userFound = dbsqLite.signIn(emailInput,passInput)

                if(userFound.isSuccess) {

                    Toast.makeText(this, "Se ha encontrado el usuario con email: ${userFound.user?.id}", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Hubo un error, revise sus credenciales", Toast.LENGTH_SHORT).show()
                }
            }catch (error: Exception) {
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}