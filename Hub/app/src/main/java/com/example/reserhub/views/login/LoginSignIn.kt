package com.example.reserhub.views.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reserhub.DataBaseController
import com.example.reserhub.MainActivity
import com.example.reserhub.R
import com.example.reserhub.views.dashboard.SuperAdmin
import com.example.reserhub.views.hub.hub

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
                Log.d("Test","$userFound")
                if(userFound.status) {
                    if(userFound.user.rol == "ADMIN") {
                        val intent = Intent(this, SuperAdmin::class.java)
                        intent.putExtra("USER_ID","${userFound.user.id}");
                        startActivity(intent)
                    } else if(userFound.user.rol == "CLIENT"){
                        val intent = Intent(this, hub::class.java)
                        Log.d("Test","${userFound.user.id}")
                        intent.putExtra("USER_ID","${userFound.user.id}");
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("USER_ID","${userFound.user.id}");
                        startActivity(intent)
                    }

                    Toast.makeText(this, "Se ha iniciado sesión con ${userFound.user.rol}", Toast.LENGTH_SHORT).show()
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