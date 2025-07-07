package org.example.hospital.notifier

data class NotificationRequest(
    val message: String,
    val email: String
)