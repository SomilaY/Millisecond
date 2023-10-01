package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Goals
import com.example.millisecond.models.Project
import com.google.firebase.database.*
import java.util.*

class GoalsActivity : AppCompatActivity() {
    private lateinit var spinnerProjectName: Spinner
    private lateinit var seekBarMinHours: SeekBar
    private lateinit var seekBarMaxHours: SeekBar
    private lateinit var buttonSubmit: Button
    private lateinit var buttonViewGoals: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goals)

        database = FirebaseDatabase.getInstance().reference

        spinnerProjectName = findViewById(R.id.spinnerProjectName)
        seekBarMinHours = findViewById(R.id.seekBarMinHours)
        seekBarMaxHours = findViewById(R.id.seekBarMaxHours)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        initSpinner()

        buttonSubmit.setOnClickListener {
            val projectName = spinnerProjectName.selectedItem.toString()
            val minHours = seekBarMinHours.progress
            val maxHours = seekBarMaxHours.progress

            val goals = Goals(projectName, minHours, maxHours)
            saveGoalsToDatabase(goals)
        }

        buttonViewGoals.setOnClickListener{
            val intent = Intent(this, DisplayGoalsActivity::class.java)
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

    private fun initSpinner() {

        val projectsRef = database.child("projects")
        projectsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val projectNames = mutableListOf<String>()
                for (projectSnapshot in dataSnapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    project?.let {
                        projectNames.add(it.projectName)
                    }
                }

                val adapter = ArrayAdapter(this@GoalsActivity, android.R.layout.simple_spinner_item, projectNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProjectName.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun saveGoalsToDatabase(goals: Goals) {
        val goalsRef = database.child("goals")
        val goalsId = goalsRef.push().key
        goalsId?.let {
            goalsRef.child(it).setValue(goals)
        }
    }
}
