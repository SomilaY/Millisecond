package com.example.millisecond

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.millisecond.models.Project

class ProjectAdapter : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    private val projects = mutableListOf<Project>()

    inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val projectNameTextView: TextView = itemView.findViewById(R.id.editTextProjectName)
        private val projectDescriptionTextView: TextView = itemView.findViewById(R.id.editTextProjectDescription)
        private val projectPhotoImageView: ImageView = itemView.findViewById(R.id.projectPhoto)

        fun bind(project: Project) {
            projectNameTextView.text = project.projectName
            projectDescriptionTextView.text = project.projectDescription


            Glide.with(itemView.context)
                .load(project.projectPhotoUri)
                .into(projectPhotoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.project_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun setProjects(projects: List<Project>) {
        this.projects.clear()
        this.projects.addAll(projects)
        notifyDataSetChanged()
    }
}
