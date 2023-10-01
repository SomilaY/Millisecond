package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Goals
import com.google.firebase.database.*


class DisplayGoalsActivity : AppCompatActivity() {

    private val homeButton = findViewById<ImageView>(R.id.HomeButton)
    private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)

    private lateinit var databaseReference: DatabaseReference
    private val spinnerAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.displaygoals)

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

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Set up the spinner adapter
        val spinnerProjectName = findViewById<Spinner>(R.id.spinnerProjectName)
        spinnerProjectName.adapter = spinnerAdapter

        // Set up the item click listener for the spinner
        spinnerProjectName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val projectName = parent?.getItemAtPosition(position).toString()
                if (projectName.isNotEmpty()) {
                    // Retrieve goals data for the selected project name
                    retrieveGoalsData(projectName)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Retrieve project names from Firebase and update the spinner
        retrieveProjectNames()
    }

    private fun retrieveProjectNames() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val projectName = snapshot.child("projectName").value as? String
                    projectName?.let {
                        spinnerAdapter.add(projectName)
                    }
                }
                spinnerAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@DisplayGoalsActivity, "Error retrieving project names", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference.child("projects").addListenerForSingleValueEvent(valueEventListener)
    }

    private fun retrieveGoalsData(projectName: String) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val goals = dataSnapshot.getValue(Goals::class.java)
                if (goals != null) {
                    displayGoalsData(goals)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@DisplayGoalsActivity, "Error retrieving goals data", Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference.child("goals").child(projectName).addListenerForSingleValueEvent(valueEventListener)
    }

    private fun displayGoalsData(goals: Goals) {
        val textMinHours = findViewById<TextView>(R.id.textMinHours)
        val textMaxHours = findViewById<TextView>(R.id.textMaxHours)

        textMinHours.text = "Min Hours: ${goals.minHours}"
        textMaxHours.text = "Max Hours: ${goals.maxHours}"
    }

}
