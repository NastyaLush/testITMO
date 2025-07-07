package org.example.hospital.core.doctor.storage

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "doctors")
class Doctor (
    @Id
    var id: Long?,

    var name: String,

    var createdAt: LocalDateTime
)
