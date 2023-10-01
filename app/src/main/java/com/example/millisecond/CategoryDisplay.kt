package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Category
import com.google.firebase.database.*

class CategoryDisplayActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var categoryNameTextView: TextView
    private lateinit var categoryHoursTextView: TextView

    private lateinit var database: DatabaseReference
    private lateinit var categoryList: MutableList<Category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)

        categorySpinner = findViewById(R.id.spinnerProjectName)
        categoryNameTextView = findViewById(R.id.categoryNameTextView)
        categoryHoursTextView = findViewById(R.id.categoryHoursTextView)

        categoryList = mutableListOf()
        database = FirebaseDatabase.getInstance().reference.child("categories")

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position) as String
                val selectedCategoryData = getCategoryByName(selectedCategory)
                if (selectedCategoryData != null) {
                    categoryNameTextView.text = selectedCategoryData.categoryName
                    categoryHoursTextView.text = selectedCategoryData.hoursSpent.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        loadCategoryNames()

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

    private fun loadCategoryNames() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(Category::class.java)
                    if (category != null) {
                        categoryList.add(category)
                    }
                }
                populateSpinner()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }

    private fun populateSpinner() {
        val categoryNames = categoryList.map { it.categoryName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun getCategoryByName(categoryName: String): Category? {
        return categoryList.find { it.categoryName == categoryName }
    }
}
