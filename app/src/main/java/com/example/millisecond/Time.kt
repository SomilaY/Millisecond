package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.R
import com.example.millisecond.models.Category
import com.example.millisecond.models.TimeEntry
import com.google.firebase.database.*
import java.time.LocalTime
import kotlin.time.Duration

class TimeEntryActivity : AppCompatActivity() {

    private lateinit var projectNameAutoCompleteTextView: AutoCompleteTextView
    private lateinit var datePickerProjectDate: DatePicker
    private lateinit var timePickerStartTime: TimePicker
    private lateinit var timePickerEndTime: TimePicker
    private lateinit var spinnerCategory: Spinner
    private lateinit var buttonSubmit: Button

    private lateinit var projectNamesAdapter: ArrayAdapter<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>

    private lateinit var database: FirebaseDatabase
    private lateinit var timeEntryRef: DatabaseReference
    private lateinit var categoryRef: DatabaseReference

    private val projectNames: MutableList<String> = mutableListOf()
    private val categories: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time)

        projectNameAutoCompleteTextView = findViewById(R.id.autoCompleteTextViewProjectName)
        datePickerProjectDate = findViewById(R.id.datePickerProjectDeadline)
        timePickerStartTime = findViewById(R.id.timePickerStartTime)
        timePickerEndTime = findViewById(R.id.timePickerEndTime)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        database = FirebaseDatabase.getInstance()
        timeEntryRef = database.getReference("time_entries")
        categoryRef = database.getReference("categories")

        loadProjectNames()
        loadCategories()

        buttonSubmit.setOnClickListener {
            saveTimeEntry()
        }

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

    private fun loadProjectNames() {
        val projectNamesQuery = timeEntryRef.orderByChild("projectName")
        projectNamesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (projectSnapshot in snapshot.children) {
                    val projectName = projectSnapshot.child("projectName").value.toString()
                    if (!projectNames.contains(projectName)) {
                        projectNames.add(projectName)
                    }
                }

                projectNamesAdapter =
                    ArrayAdapter(this@TimeEntryActivity, android.R.layout.simple_dropdown_item_1line, projectNames)
                projectNameAutoCompleteTextView.setAdapter(projectNamesAdapter)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun loadCategories() {
        val categoriesQuery = categoryRef.orderByChild("categoryName")
        categoriesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("categoryName").value.toString()
                    if (!categories.contains(categoryName)) {
                        categories.add(categoryName)
                    }
                }

                categoryAdapter =
                    ArrayAdapter(this@TimeEntryActivity, android.R.layout.simple_spinner_item, categories)
                spinnerCategory.adapter = categoryAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun saveTimeEntry() {
        val projectName = projectNameAutoCompleteTextView.text.toString()
        val day = datePickerProjectDate.dayOfMonth
        val month = datePickerProjectDate.month + 1
        val year = datePickerProjectDate.year
        val date = "$day/$month/$year"
        val startTime = "${timePickerStartTime.hour}:${timePickerStartTime.minute}"
        val endTime = "${timePickerEndTime.hour}:${timePickerEndTime.minute}"
        val category = spinnerCategory.selectedItem.toString()

        val startHour = timePickerStartTime.hour
        val startMinute = timePickerStartTime.minute
        val endHour = timePickerEndTime.hour
        val endMinute = timePickerEndTime.minute

        val hoursSpent = calculateHoursSpent(startHour, startMinute, endHour, endMinute)

        val timeEntry = TimeEntry(projectName, startTime, endTime, category, hoursSpent)

        val timeEntryKey = timeEntryRef.push().key
        timeEntryKey?.let {
            timeEntryRef.child(it).setValue(timeEntry)
                .addOnSuccessListener {
                    Toast.makeText(this, "Time entry saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save time entry", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun calculateHoursSpent(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int): Int {
        val startMinutes = startHour * 60 + startMinute
        val endMinutes = endHour * 60 + endMinute
        val minutesSpent = endMinutes - startMinutes
        return minutesSpent / 60
    }

}
