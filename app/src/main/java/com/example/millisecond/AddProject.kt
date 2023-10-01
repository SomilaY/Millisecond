package com.example.millisecond

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.millisecond.models.Project
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import com.google.firebase.storage.FirebaseStorage

class ProjectActivity : AppCompatActivity() {

    private lateinit var editTextProjectName: EditText
    private lateinit var editTextProjectDescription: EditText
    private lateinit var editTextProjectClient: EditText
    private lateinit var datePickerProjectDeadline: DatePicker
    private lateinit var buttonAddPhoto: Button
    private lateinit var buttonSubmit: Button
    private var projectPhotoUri: Uri? = null

    private val homeButton = findViewById<ImageView>(R.id.HomeButton)
    private val timeButton = findViewById<ImageView>(R.id.timeButton)
    private val expenseButton = findViewById<ImageView>(R.id.expenseButton)
    private val reportButton = findViewById<ImageView>(R.id.reportButton)
    private val userButton = findViewById<ImageView>(R.id.userButton)

    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addproject)

        editTextProjectName = findViewById(R.id.editTextProjectName)
        editTextProjectDescription = findViewById(R.id.editTextProjectDescription)
        editTextProjectClient = findViewById(R.id.editTextProjectClient)
        datePickerProjectDeadline = findViewById(R.id.datePickerProjectDeadline)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        buttonAddPhoto.setOnClickListener {
            openImagePicker()
        }

        buttonSubmit.setOnClickListener {
            saveProject()
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

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                projectPhotoUri = it

                val imageView = findViewById<ImageView>(R.id.imageHolder)
                imageView.setImageURI(projectPhotoUri)
            }
        }
    }

    private fun saveProject() {
        val projectName = editTextProjectName.text.toString()
        val projectDescription = editTextProjectDescription.text.toString()
        val projectClient = editTextProjectClient.text.toString()


        if (projectName.isBlank() || projectDescription.isBlank() || projectClient.isBlank()) {
            showToast("Please fill in all fields")
            return
        }

        val calendar = Calendar.getInstance()
        calendar.set(datePickerProjectDeadline.year, datePickerProjectDeadline.month, datePickerProjectDeadline.dayOfMonth)
        val projectDeadline = calendar.time

        val project = Project(projectName, projectDescription, projectClient, projectDeadline, null)

        val projectId = database.reference.child("projects").push().key
        projectId?.let {
            val projectReference = database.reference.child("projects").child(it)


            if (projectPhotoUri != null) {

                val storageReference = FirebaseStorage.getInstance().reference.child("project_photos").child(projectId)
                val uploadTask = storageReference.putFile(projectPhotoUri!!)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        project.projectPhotoUri = downloadUri.toString()
                        projectReference.setValue(project)
                            .addOnSuccessListener {
                                showToast("Project saved successfully")
                                clearForm()
                            }
                            .addOnFailureListener {
                                showToast("Failed to save project")
                            }
                    } else {
                        showToast("Failed to save project")
                    }
                }
            } else {
                projectReference.setValue(project)
                    .addOnSuccessListener {
                        showToast("Project saved successfully")
                        clearForm()
                    }
                    .addOnFailureListener {
                        showToast("Failed to save project")
                    }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearForm() {
        editTextProjectName.text.clear()
        editTextProjectDescription.text.clear()
        editTextProjectClient.text.clear()
        projectPhotoUri = null
        val imageView = findViewById<ImageView>(R.id.Banner)
        imageView.setImageResource(R.drawable.millisecond)
    }

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }
}
