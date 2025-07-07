package org.example.notifier.dto

data class NotificationRequest(
    val message: String,
    val email: String
)