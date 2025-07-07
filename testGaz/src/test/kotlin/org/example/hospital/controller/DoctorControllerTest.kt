package org.example.hospital.controller

import org.example.hospital.generated.api.model.DoctorCreateDto
import org.example.hospital.AbstractTest
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class DoctorControllerTest : AbstractTest() {

    @Test
    fun getDoctorByIdTest() {
        val createdDoctor = createDoctor()
        mockMvc.get("/api/v1/doctor/${createdDoctor.id}") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(createdDoctor.id) }
                jsonPath("$.name") { value(createdDoctor.name) }
            }
    }

    @Test
    fun createDoctorTest() {
        val request = DoctorCreateDto(
            name = "New Doctor"
        )

        mockMvc.post("/api/v1/doctor") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { exists() }
                jsonPath("$.name") { value(request.name) }
            }
    }

    @Test
    fun updateDoctorTest() {
        val createdDoctor = createDoctor()
        val request = DoctorCreateDto(
            name = "New Doctor"
        )

        mockMvc.put("/api/v1/doctor/${createdDoctor.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(createdDoctor.id) }
                jsonPath("$.name") { value(request.name) }
            }
    }

    @Test
    fun deleteDoctorTest() {
        val createdDoctor = createDoctor()
        mockMvc.delete("/api/v1/doctor/${createdDoctor.id}")
            .andExpect {
                status { isNoContent() }
            }

        mockMvc.get("/api/v1/doctor/${createdDoctor.id}") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun getAllDoctorsTest() {
        createDoctor()
        createDoctor()
        mockMvc.get("/api/v1/doctor") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { hasSize<Any>(2) }
            }
    }

}