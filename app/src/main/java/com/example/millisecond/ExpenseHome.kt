package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Expense
import com.example.millisecond.models.Project
import com.google.firebase.database.*

class ExpenseHomeActivity : AppCompatActivity() {

    private lateinit var spinnerProjectName: Spinner
    private lateinit var editTextExpenseDescription: EditText
    private lateinit var editTextExpenseDate: EditText
    private lateinit var editTextExpensePrice: EditText
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var projectsReference: DatabaseReference

    private val homeButton = findViewById<ImageView>(R.id.HomeButton)
    private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)
    private val Add = findViewById<ImageView>(R.id.btnAdd)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expensehome)

        firebaseDatabase = FirebaseDatabase.getInstance()
        projectsReference = firebaseDatabase.getReference("projects")

        spinnerProjectName = findViewById(R.id.spinnerProjectName)
        editTextExpenseDescription = findViewById(R.id.editTextExpenseDescription)
        editTextExpenseDate = findViewById(R.id.editTextExpenseDate)
        editTextExpensePrice = findViewById(R.id.editTextExpensePrice)

        loadProjectNames()

        spinnerProjectName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val projectName = parent?.getItemAtPosition(position).toString()
                retrieveExpenseDetails(projectName)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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

        Add.setOnClickListener{
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadProjectNames() {
        projectsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val projectNames = mutableListOf<String>()
                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    project?.let {
                        projectNames.add(it.projectName)
                    }
                }

                val adapter = ArrayAdapter(
                    this@ExpenseHomeActivity,
                    android.R.layout.simple_spinner_item,
                    projectNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProjectName.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun retrieveExpenseDetails(projectName: String) {
        val expensesReference = firebaseDatabase.getReference("expenses")
        expensesReference.child(projectName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expense = snapshot.getValue(Expense::class.java)
                expense?.let {
                    editTextExpenseDescription.setText(it.expenseDescription)
                    editTextExpenseDate.setText(it.expenseDate as CharSequence?)
                    editTextExpensePrice.setText(it.expensePrice as CharSequence?)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
