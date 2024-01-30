package com.example.crypto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crypto.databinding.ActivityCreateNewBinding
import com.google.firebase.auth.FirebaseAuth


class CreateNew : AppCompatActivity() {

    lateinit var Auth: FirebaseAuth

    private lateinit var binding: ActivityCreateNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Auth = FirebaseAuth.getInstance()
        binding.buttonOk.setOnClickListener {
            signUpUser()
        }
    }


    private fun signUpUser():Unit = with(binding){
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this@CreateNew, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this@CreateNew, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener() {
            if (it.isSuccessful) {
                Toast.makeText(this@CreateNew, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@CreateNew, LoginAcc::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@CreateNew, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}