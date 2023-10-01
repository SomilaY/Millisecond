package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Expense
import com.example.millisecond.models.Project
import com.google.firebase.database.*
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var spinnerProjectName: Spinner
    private lateinit var datePickerExpenseDate: DatePicker
    private lateinit var editTextExpensePrice: EditText
    private lateinit var editTextExpenseDescription: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var database: DatabaseReference

    private val homeButton = findViewById<ImageView>(R.id.HomeButton)
    private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addexpense)

        spinnerProjectName = findViewById(R.id.spinnerProjectName)
        datePickerExpenseDate = findViewById(R.id.datePickerExpenseDate)
        editTextExpensePrice = findViewById(R.id.editTextExpensePrice)
        editTextExpenseDescription = findViewById(R.id.editTextExpenseDescription)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        database = FirebaseDatabase.getInstance().reference

        loadProjectNames()

        buttonSubmit.setOnClickListener {
            addExpense()
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

    private fun loadProjectNames() {

        database.child("projects").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val projectNames = mutableListOf<String>()

                for (projectSnapshot in dataSnapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    project?.projectName?.let {
                        projectNames.add(it)
                    }
                }

                val adapter = ArrayAdapter(
                    this@AddExpenseActivity,
                    android.R.layout.simple_spinner_item,
                    projectNames
                )
                spinnerProjectName.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun addExpense() {
        val projectName = spinnerProjectName.selectedItem.toString()

        val day = datePickerExpenseDate.dayOfMonth
        val month = datePickerExpenseDate.month
        val year = datePickerExpenseDate.year
        val expenseDate = getDateFromDatePicker(day, month, year)

        val expensePrice = editTextExpensePrice.text.toString().toDouble()

        val expenseDescription = editTextExpenseDescription.text.toString()

        val expense = Expense(projectName, expenseDescription, expenseDate, expensePrice)

        val expenseId = database.child("expense").push().key
        expenseId?.let {
            database.child("expense").child(it).setValue(expense)
                .addOnSuccessListener {

                    Toast.makeText(this@AddExpenseActivity, "Expense added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {

                    Toast.makeText(this@AddExpenseActivity, "Failed to add expense", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getDateFromDatePicker(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        return calendar.time
    }
}
