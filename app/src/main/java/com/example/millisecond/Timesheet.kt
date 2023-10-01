package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.millisecond.models.TimeEntry
import com.google.firebase.database.*


class TimesheetActivity : AppCompatActivity() {
    private lateinit var timeEntryAdapter: TimeEntryAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timesheet)

        val timeEntryRecyclerView: RecyclerView = findViewById(R.id.timeEntryRecyclerView)
        timeEntryRecyclerView.layoutManager = LinearLayoutManager(this)

        databaseRef = FirebaseDatabase.getInstance().reference.child("time_entries")
        timeEntryAdapter = TimeEntryAdapter()
        timeEntryRecyclerView.adapter = timeEntryAdapter

        // Fetch data from Firebase
        fetchData()

        findViewById<Button>(R.id.HomeButton).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.timeButton).setOnClickListener {
            val intent = Intent(this, TimeEntryActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.expenseButton).setOnClickListener {
            val intent = Intent(this, ExpenseHomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.reportButton).setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.userButton).setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchData() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val timeEntries = mutableListOf<TimeEntry>()
                for (snapshot in dataSnapshot.children) {
                    val timeEntry = snapshot.getValue(TimeEntry::class.java)
                    timeEntry?.let {
                        timeEntries.add(it)
                    }
                }
                timeEntryAdapter.setTimeEntries(timeEntries)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}
