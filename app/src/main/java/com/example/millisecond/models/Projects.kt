package com.example.millisecond.models

import java.util.Date

data class Project(
    var projectName: String,
    var projectDescription: String,
    var projectClient: String,
    var projectDeadline: Date,
    var projectPhotoUri: String?
)