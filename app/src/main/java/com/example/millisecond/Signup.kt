package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)


        firebaseDatabase = FirebaseDatabase.getInstance()

        emailEditText = findViewById(R.id.Email)
        usernameEditText = findViewById(R.id.Username)
        passwordEditText = findViewById(R.id.Password)
        signupButton = findViewById(R.id.btnSignUp)

        signupButton.setOnClickListener{
            signUp()
        }

        val signInTextView: TextView = findViewById(R.id.textSignIn)
        signInTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    private fun signUp() {
        val email = emailEditText.text.toString().trim()
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val usersRef = firebaseDatabase.getReference("Users")
        usersRef.orderByChild("username").equalTo(username).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                showToast("Username already exists")
            } else {
                val newUser = User(username, password, email)
                usersRef.child(username).setValue(newUser)
                    .addOnSuccessListener {
                        showToast("Sign up successful")
                        val intent = Intent(this@SignupActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { exception ->
                        showToast("Sign up failed: ${exception.message}")
                    }
            }
        }.addOnFailureListener { exception ->
            showToast("Sign up failed: ${exception.message}")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}