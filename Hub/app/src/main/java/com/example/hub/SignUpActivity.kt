package com.example.hub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        Toast.makeText(this, "Estás en Sign Up", Toast.LENGTH_SHORT).show()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var dbsqLite = DBSQLite(this)

        var signUpBtn = findViewById<Button>(R.id.loginBtn)

        signUpBtn.setOnClickListener{
            try {
                var nameInput: String = findViewById<EditText>(R.id.nameEditText).text.toString()
                var emailInput: String = findViewById<EditText>(R.id.emailEditText).text.toString()
                var passInput: String = findViewById<EditText>(R.id.passEditText).text.toString()
                var newUser = User(0,nameInput, emailInput, passInput)

                val isCreated = dbsqLite.signUp(newUser)
                if(isCreated.isSuccess) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, isCreated.message, Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, isCreated.message, Toast.LENGTH_SHORT).show()
                }
            }catch (error: Exception) {
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}