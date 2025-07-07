package org.example.hospital.controller

import org.example.hospital.generated.api.model.PatientCreateDto
import org.example.hospital.AbstractTest
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class PatientControllerTest : AbstractTest() {

    @Test
    fun getPatientByIdTest() {
        val createdPatient = createPatient()
        mockMvc.get("/api/v1/patient/${createdPatient.id}") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(createdPatient.id.toString()) }
                jsonPath("$.name") { value(createdPatient.name) }
                jsonPath("$.email") { value(createdPatient.email) }
                jsonPath("$.createdAt") { exists() }
            }
    }

    @Test
    fun createPatientTest() {
        val request = PatientCreateDto(
            name = "Patient name",
            email = "patientEmail@gmail.com"
        )

        mockMvc.post("/api/v1/patient") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value("Patient name") }
                jsonPath("$.email") { value("patientEmail@gmail.com") }
            }

    }

    @Test
    fun updatePatientTest() {
        val createdPatient = createPatient()
        val request = PatientCreateDto(
            name = "Patient name",
            email = "patientEmail@gmail.com"
        )

        mockMvc.put("/api/v1/patient/${createdPatient.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(createdPatient.id.toString()) }
                jsonPath("$.name") { value(request.name) }
                jsonPath("$.email") { value(request.email) }
                jsonPath("$.createdAt") { value(createdPatient.createdAt.toString()) }
            }
    }

    @Test
    fun deletePatientTest() {
        val createdPatient = createPatient()
        mockMvc.delete("/api/v1/patient/${createdPatient.id}")
            .andExpect {
                status { isNoContent() }
            }
        mockMvc.get("/api/v1/patient/${createdPatient.id}") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun getAllPatientsTestTest() {
        createPatient()
        createPatient()
        mockMvc.get("/api/v1/patient") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { hasSize<Any>(2) }
            }
    }
}