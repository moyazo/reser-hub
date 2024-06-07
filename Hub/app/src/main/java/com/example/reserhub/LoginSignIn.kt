package com.example.reserhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginSignIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var db = DataBaseController(this);
        var signInBtn: Button = findViewById<Button>(R.id.signInBtn);

        signInBtn.setOnClickListener{
            try {
                var emailValue: String = findViewById<EditText>(R.id.EmailInput).text.toString();
                var passValue: String = findViewById<EditText>(R.id.PasswordInput).text.toString();

                val userFound = db.logIn(emailValue,passValue)
                if(userFound.status) {

                    Toast.makeText(this, "Se ha encontrado el usuario con email: ${userFound.user.email}", Toast.LENGTH_SHORT).show()
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