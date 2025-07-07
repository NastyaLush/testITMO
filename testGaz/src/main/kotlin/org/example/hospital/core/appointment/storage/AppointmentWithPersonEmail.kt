package org.example.hospital.core.appointment.storage

import java.time.OffsetDateTime

class AppointmentWithPersonEmail(

    var doctorName: String,

    val email: String,

    var startedAt: OffsetDateTime,

    var finishedAt: OffsetDateTime,

    )