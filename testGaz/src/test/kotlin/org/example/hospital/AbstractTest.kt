package org.example.hospital

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.tomakehurst.wiremock.client.WireMock
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.example.hospital.core.appointment.storage.AppointmentRepository
import org.example.hospital.core.doctor.storage.DoctorRepository
import org.example.hospital.core.patient.storage.PatientRepository
import org.example.hospital.generated.api.model.*
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.time.Clock
import java.time.OffsetDateTime
import java.util.*

@SpringBootTest(classes = [VeryExpensiveHospital::class])
@AutoConfigureMockMvc
@AutoConfigureWireMock
@ActiveProfiles("test")
abstract class AbstractTest {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
    }

    protected fun createPatient(): PatientResponseDto {
        val request = PatientCreateDto(
            name = "Test Patient ${UUID.randomUUID()}",
            email = "${UUID.randomUUID()}@test.com"
        )

        return mockMvc.post("/api/v1/patient") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value(request.name) }
            jsonPath("$.email") { value(request.email) }
        }.andReturn().let {
            val ctx: DocumentContext = JsonPath.parse(it.response.contentAsString)
            PatientResponseDto(
                ctx.read<Any>("id").toString().toLong(),
                ctx.read<Any>("name").toString(),
                ctx.read<Any>("email").toString(),
                OffsetDateTime.parse(ctx.read<Any>("createdAt").toString())
            )
        }
    }

    protected fun getPatient(id: Long): PatientResponseDto {
        return mockMvc.get("/api/v1/patient/${id}") {
            accept = MediaType.APPLICATION_JSON
        }.andReturn().let {
            val ctx: DocumentContext = JsonPath.parse(it.response.contentAsString)
            PatientResponseDto(
                ctx.read<Any>("id").toString().toLong(),
                ctx.read<Any>("name").toString(),
                ctx.read<Any>("email").toString(),
                OffsetDateTime.parse(ctx.read<Any>("createdAt").toString())
            )
        }
    }

    protected fun createDoctor(): DoctorResponseDto {
        val request = DoctorCreateDto(
            name = "Dr. ${UUID.randomUUID()}"
        )

        val post = mockMvc.post("/api/v1/doctor") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
        return post.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value(request.name) }
        }.andReturn().let {
            val ctx: DocumentContext = JsonPath.parse(it.response.contentAsString)
            DoctorResponseDto(
                ctx.read<Any>("id").toString().toLong(),
                ctx.read<Any>("name").toString()
            )
        }
    }

    protected fun getDoctor(id: Long): DoctorResponseDto {
        return mockMvc.get("/api/v1/doctor/${id}") {
            accept = MediaType.APPLICATION_JSON
        }.andReturn().let {
            val ctx: DocumentContext = JsonPath.parse(it.response.contentAsString)
            DoctorResponseDto(
                ctx.read<Any>("id").toString().toLong(),
                ctx.read<Any>("name").toString()
            )
        }
    }

    protected fun createAppointment(
        status: Status,
        startedAt: OffsetDateTime = OffsetDateTime.now(clock),
        durationMinutes: Long = 30
    ): AppointmentResponseDto {
        val createdDoctor = createDoctor()
        val createdPatient = if (status == Status.BOOKED) createPatient() else null

        val request = AppointmentCreateDto(
            doctor = createdDoctor.id,
            startedAt = startedAt,
            finishedAt = startedAt.plusMinutes(durationMinutes),
            status = status,
            patient = createdPatient?.id
        )

        return mockMvc.post("/api/v1/appointment") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { exists() }
            jsonPath("$.doctor") { value(request.doctor) }
            jsonPath("$.startedAt") { exists() }
            jsonPath("$.finishedAt") { exists() }
            jsonPath("$.status") { value(status.toString()) }
            if (status == Status.BOOKED) {
                jsonPath("$.patient") { value(request.patient) }
            } else {
                jsonPath("$.patient") { isEmpty() }
            }
        }.andReturn().let {
            val ctx: DocumentContext = JsonPath.parse(it.response.contentAsString)
            AppointmentResponseDto(
                ctx.read<Any>("id").toString().toLong(),
                ctx.read<Any>("doctor").toString().toLong(),
                OffsetDateTime.parse(ctx.read<Any>("startedAt").toString()),
                OffsetDateTime.parse(ctx.read<Any>("finishedAt").toString()),
                Status.valueOf(ctx.read<Any>("status").toString()),
                ctx.read<Any?>("patient")?.toString()?.toLongOrNull()
            )
        }
    }


    @AfterEach
    fun tearDown() {
        appointmentRepository.deleteAll()
        patientRepository.deleteAll()
        doctorRepository.deleteAll()
        WireMock.resetAllRequests()
        WireMock.resetToDefault()
    }

    protected val objectMapper: JsonMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
        .disable(com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
        .build()
//    ObjectMapper().apply {
//        disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
//        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//    }

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var doctorRepository: DoctorRepository

    @Autowired
    protected lateinit var patientRepository: PatientRepository

    @Autowired
    protected lateinit var appointmentRepository: AppointmentRepository

    @Autowired
    protected lateinit var clock: Clock
}