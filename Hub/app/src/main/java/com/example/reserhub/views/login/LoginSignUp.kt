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

                    if(created.user.rol == "CLIENT") {
                        Log.d("Rol","${created.user.rol}")
                        val intent = Intent(this, hub::class.java)
                        intent.putExtra("USER_ID","${created.user.id}");
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, SuperAdmin::class.java)
                        intent.putExtra("USER_ID","${created.user.id}");
                        startActivity(intent)
                        Toast.makeText(this, "Se ha iniciado sesión con ${created.response}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "No se pudo iniciar sesión ${created.response}", Toast.LENGTH_SHORT).show()
                }
            }catch (error: Exception){
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
