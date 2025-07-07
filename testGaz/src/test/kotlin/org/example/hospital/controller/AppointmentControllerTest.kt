package org.example.hospital.controller

import org.example.hospital.generated.api.model.AppointmentCreateDto
import org.example.hospital.generated.api.model.Status
import org.example.hospital.AbstractTest
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.OffsetDateTime

class AppointmentControllerTest : AbstractTest() {
    @Test
    fun createFreeAppointment() {
        val createdDoctor = createDoctor()
        val request = AppointmentCreateDto(
            doctor = createdDoctor.id,
            patient = null,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.FREE
        )

        mockMvc.post("/api/v1/appointment") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.doctor") { value(request.doctor) }
                jsonPath("$.startedAt") { exists() }
                jsonPath("$.finishedAt") { exists() }
                jsonPath("$.status") { value(request.status.toString()) }
                jsonPath("$.patient") { value(request.patient) }
            }
    }

    @Test
    fun createBookedAppointment() {
        val createdDoctor = createDoctor()
        val createdPatient = createPatient()
        val request = AppointmentCreateDto(
            doctor = createdDoctor.id,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.BOOKED,
            patient = createdPatient.id
        )

        mockMvc.post("/api/v1/appointment") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.doctor") { value(request.doctor) }
                jsonPath("$.startedAt") { exists() }
                jsonPath("$.finishedAt") { exists() }
                jsonPath("$.status") { value(request.status.toString()) }
                jsonPath("$.patient") { value(request.patient) }
            }
    }

    @Test
    fun createFreeAppointmentWrongRequest() {
        val createdDoctor = createDoctor()
        val createdPatient = createPatient()
        val request = AppointmentCreateDto(
            doctor = createdDoctor.id,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.FREE,
            patient = createdPatient.id
        )

        mockMvc.post("/api/v1/appointment") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun createBookedAppointmentWrongRequest() {
        val createdDoctor = createDoctor()
        val request = AppointmentCreateDto(
            doctor = createdDoctor.id,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.BOOKED
        )

        mockMvc.post("/api/v1/appointment") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun updateFreeAppointmentTest() {
        val createdPatient = createPatient()
        val createdAppointment = createAppointment(Status.FREE)
        val request = AppointmentCreateDto(
            doctor = createdAppointment.doctor,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.BOOKED,
            patient = createdPatient.id
        )

        mockMvc.put("/api/v1/appointment/${createdAppointment.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.doctor") { value(request.doctor) }
                jsonPath("$.startedAt") { exists() }
                jsonPath("$.finishedAt") { exists() }
                jsonPath("$.status") { value(request.status.toString()) }
                jsonPath("$.patient") { value(request.patient) }
            }
    }

    @Test
    fun updateBookedAppointmentTest() {
        val createdAppointment = createAppointment(Status.BOOKED)
        val request = AppointmentCreateDto(
            doctor = createdAppointment.doctor,
            startedAt = OffsetDateTime.now(clock),
            finishedAt = OffsetDateTime.now(clock).plusMinutes(30),
            status = Status.FREE
        )

        mockMvc.put("/api/v1/appointment/${createdAppointment.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.doctor") { value(request.doctor) }
                jsonPath("$.startedAt") { exists() }
                jsonPath("$.finishedAt") { exists() }
                jsonPath("$.status") { value(request.status.toString()) }
                jsonPath("$.patient") { value(request.patient) }
            }
    }


    @Test
    fun getAppointmentByIdTest() {
        val createdAppointment = createAppointment(Status.FREE)
        mockMvc.get("/api/v1/appointment/${createdAppointment.id}")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(createdAppointment.id) }
                jsonPath("$.doctor") { value(createdAppointment.doctor) }
                jsonPath("$.startedAt") { exists() }
                jsonPath("$.finishedAt") { exists() }
                jsonPath("$.status") { value(createdAppointment.status.toString()) }
                jsonPath("$.patient") { value(createdAppointment.patient) }
            }
    }

    @Test
    fun deleteDepartmentTest() {
        val createdAppointment = createAppointment(Status.FREE)
        mockMvc.delete("/api/v1/appointment/${createdAppointment.id}")
            .andExpect {
                status { isNoContent() }
            }

        mockMvc.get("/api/v1/appointment/${createdAppointment.id}")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun getDoctorAppointmentsTest() {
        val createdAppointment = createAppointment(Status.FREE)
        mockMvc.get("/api/v1/appointment/doctor/${createdAppointment.doctor}")
            .andExpect {
                status { isOk() }
                jsonPath("$") { hasSize<Any>(1) }
            }
    }

    @Test
    fun getPatientAppointmentsTest() {
        val createdAppointment = createAppointment(Status.BOOKED)
        mockMvc.get("/api/v1/appointment/patient/${createdAppointment.patient}")
            .andExpect {
                status { isOk() }
                jsonPath("$") { hasSize<Any>(1) }
            }
    }
}