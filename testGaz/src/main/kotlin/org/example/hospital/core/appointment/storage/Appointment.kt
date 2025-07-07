package org.example.hospital.core.appointment.storage

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.OffsetDateTime

@Table(name = "appointments")
class Appointment(

    @Id
    var id: Long,

    var doctor: Long,

    var patient: Long?,

    var status: Status,

    var startedAt: OffsetDateTime,

    var finishedAt: OffsetDateTime,

    ) {
    enum class Status {
        FREE,
        BOOKED
    }
}