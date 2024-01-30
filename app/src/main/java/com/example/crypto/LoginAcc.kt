package com.example.crypto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crypto.databinding.ActivityLoginAccBinding
import com.google.firebase.auth.FirebaseAuth

class LoginAcc : AppCompatActivity() {

    lateinit var Auth: FirebaseAuth
    private lateinit var binding: ActivityLoginAccBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAccBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Auth = FirebaseAuth.getInstance()

        try {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

            val userId = sharedPreferences.getString("user_id", null)
            val userEmail = sharedPreferences.getString("user_email", null)

            if (userId != null && userEmail != null) {
                var intent = Intent(this@LoginAcc, MainActivity::class.java)
                startActivity(intent)
            } else {

            }

        } catch (ex: Exception) {}

        binding.notAcc.setOnClickListener {
            var intent = Intent(this@LoginAcc, CreateNew::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        binding.buttonOk.setOnClickListener {
            signUpUser()
        }

    }


    private fun signUpUser():Unit = with(binding){
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(this@LoginAcc, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(this@LoginAcc, "Successfully Singed In", Toast.LENGTH_SHORT).show()

                val currentUser = FirebaseAuth.getInstance().currentUser
                val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("user_id", currentUser?.uid)
                editor.putString("user_email", currentUser?.email)

                editor.apply()

                var intent = Intent(this@LoginAcc, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                Toast.makeText(this@LoginAcc, "Singed In Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}