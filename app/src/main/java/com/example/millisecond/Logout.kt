package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private val homeButton = findViewById<ImageView>(R.id.HomeButton)
    private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logout)

        val logout = findViewById<Button>(R.id.logoutButton)

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        timeButton.setOnClickListener {
            val intent = Intent(this, TimeEntryActivity::class.java)
            startActivity(intent)
        }

        expenseButton.setOnClickListener {
            val intent = Intent(this, ExpenseHomeActivity::class.java)
            startActivity(intent)
        }

        reportButton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        userButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }
}