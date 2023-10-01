package com.example.millisecond

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.millisecond.models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        Log.d("Firebase", "Firebase initialized successfully")

        firebaseDatabase = FirebaseDatabase.getInstance()

        usernameEditText = findViewById(R.id.Username)
        passwordEditText = findViewById(R.id.Password)
        loginButton = findViewById(R.id.btnLogin)

        loginButton.setOnClickListener {
            login()
        }

        val signUpTextView: TextView = findViewById(R.id.textSignUp)
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        Log.d("LoginButton", "Login button clicked")
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        val usersRef = firebaseDatabase.getReference("Users")
        val query = usersRef.orderByChild("username").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    val user = dataSnapshot.children.first().getValue(User::class.java)
                    if (user != null && user.password == password) {
                        showToast("Login successful")
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        showToast("Invalid username or password")
                    }
                } else {
                    showToast("User not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Login failed: ${databaseError.message}")
            }
        })
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
