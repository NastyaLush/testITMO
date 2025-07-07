package org.example.hospital.core.patient.storage

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "patients")
class Patient(

    @Id
    var id: Long,

    var name: String,

    var email: String,

    var createdAt: LocalDateTime
)