package com.example.millisecond.models

import java.util.Date

data class Expense(
    val projectName: String,
    val expenseDescription: String,
    val expenseDate: Date,
    val expensePrice: Double
)