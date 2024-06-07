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

class LoginSignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var db = DataBaseController(this);
        var signUpBtn: Button = findViewById<Button>(R.id.signUpBtn);

        signUpBtn.setOnClickListener{
            try {
                var nameValue = findViewById<EditText>(R.id.NameInput).text.toString();
                var emailValue = findViewById<EditText>(R.id.EmailInput).text.toString();
                var passValue = findViewById<EditText>(R.id.PasswordInput).text.toString();

                val created = db.signUp(emailValue,nameValue,passValue);
                if(created.status) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, created.response, Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, LoginSignUp::class.java)
                    startActivity(intent)
                    Toast.makeText(this, created.response, Toast.LENGTH_SHORT).show()
                }
            }catch (error: Exception){
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
