package com.example.millisecond.models

data class TimeEntry(
    val projectName: String,
    val startTime: String,
    val endTime: String,
    val category: String,
    val hoursSpent: Int
)