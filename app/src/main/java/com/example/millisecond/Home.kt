package com.example.millisecond

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.millisecond.models.Project
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

   private val homeButton = findViewById<ImageView>(R.id.HomeButton)
   private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)
    private val Add = findViewById<ImageView>(R.id.btnAdd)

    private lateinit var editTextSearch: EditText
    private lateinit var recyclerViewProjects: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter

    private val database = FirebaseDatabase.getInstance().reference.child("projects")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        editTextSearch = findViewById(R.id.editTextSearch)
        recyclerViewProjects = findViewById(R.id.projectsRecyclerView)

        projectAdapter = ProjectAdapter()

        val layoutManager = LinearLayoutManager(this)
        recyclerViewProjects.layoutManager = layoutManager
        recyclerViewProjects.adapter = projectAdapter


        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            searchProjects()
        }


        retrieveProjects()

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
            val intent = Intent(this, ProjectActivity::class.java)
            startActivity(intent)
        }

    }

    private fun retrieveProjects() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val projects = mutableListOf<Project>()
                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    project?.let {
                        projects.add(it)
                    }
                }
                projectAdapter.setProjects(projects)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun searchProjects() {
        val query = editTextSearch.text.toString().trim()

        if (query.isNotEmpty()) {
            val searchQuery = database.orderByChild("projectName")
                .startAt(query)
                .endAt(query + "\uf8ff")

            searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val projects = mutableListOf<Project>()
                    for (projectSnapshot in snapshot.children) {
                        val project = projectSnapshot.getValue(Project::class.java)
                        project?.let {
                            projects.add(it)
                        }
                    }
                    projectAdapter.setProjects(projects)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else {
            projectAdapter.setProjects(emptyList())
        }
    }

    }






