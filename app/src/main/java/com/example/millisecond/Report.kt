package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reports)

        val categoriesImage = findViewById<ImageView>(R.id.Categories)
        val goalsImage = findViewById<ImageView>(R.id.goals)
        val timesheetImage = findViewById<ImageView>(R.id.timesheet)

        categoriesImage.setOnClickListener {
            val intent = Intent(this, CategoryDisplayActivity::class.java)
            startActivity(intent)
        }

        goalsImage.setOnClickListener {
            val intent = Intent(this, GoalsActivity::class.java)
            startActivity(intent)
        }

        timesheetImage.setOnClickListener {
            val intent = Intent(this, TimesheetActivity::class.java)
            startActivity(intent)
        }

        val homeButton = findViewById<ImageView>(R.id.HomeButton)
        val timeButton = findViewById<ImageView>(R.id.timeButton)
        val expenseButton = findViewById<ImageView>(R.id.expenseButton)
        val reportButton = findViewById<ImageView>(R.id.reportButton)
        val userButton = findViewById<ImageView>(R.id.userButton)

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