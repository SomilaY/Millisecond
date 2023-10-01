package com.example.millisecond

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.millisecond.models.TimeEntry

class TimeEntryAdapter : RecyclerView.Adapter<TimeEntryAdapter.TimeEntryViewHolder>() {
    private val timeEntryList: MutableList<TimeEntry> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeEntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_entry, parent, false)
        return TimeEntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeEntryViewHolder, position: Int) {
        val timeEntry = timeEntryList[position]
        holder.bind(timeEntry)
    }

    override fun getItemCount(): Int {
        return timeEntryList.size
    }

    fun setTimeEntries(timeEntries: List<TimeEntry>) {
        timeEntryList.clear()
        timeEntryList.addAll(timeEntries)
        notifyDataSetChanged()
    }

    inner class TimeEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val projectNameTextView: TextView = itemView.findViewById(R.id.textViewProjectName)
        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        private val startTimeTextView: TextView = itemView.findViewById(R.id.textViewStartTime)
        private val endTimeTextView: TextView = itemView.findViewById(R.id.textViewEndTime)
        private val hoursSpentTextView: TextView = itemView.findViewById(R.id.textViewHoursSpent)

        fun bind(timeEntry: TimeEntry) {
            projectNameTextView.text = timeEntry.projectName
            categoryTextView.text = timeEntry.category
            startTimeTextView.text = timeEntry.startTime
            endTimeTextView.text = timeEntry.endTime
            hoursSpentTextView.text = timeEntry.hoursSpent.toString()
        }
    }
}
