package org.example.hospital.core.appointment.web

import org.example.hospital.core.appointment.AppointmentService
import org.example.hospital.generated.api.AppointmentManagementApi
import org.example.hospital.generated.api.model.AppointmentCreateDto
import org.example.hospital.generated.api.model.AppointmentResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AppointmentController(
    private val appointmentServiceIml: AppointmentService
) : AppointmentManagementApi {

    override fun createAppointment(appointmentCreateDto: AppointmentCreateDto): ResponseEntity<AppointmentResponseDto> {
        val dto = appointmentServiceIml.createAppointment(appointmentCreateDto)
        return ResponseEntity.status(201).body(dto)
    }

    override fun deleteAppointment(id: Long): ResponseEntity<Unit> {
        appointmentServiceIml.deleteAppointment(id)
        return ResponseEntity.noContent().build()
    }

    override fun getAppointmentById(id: Long): ResponseEntity<AppointmentResponseDto> {
        return ResponseEntity.ok(appointmentServiceIml.getAppointmentById(id))
    }

    override fun getAppointmentsByDoctorId(doctorId: Long): ResponseEntity<List<AppointmentResponseDto>> {
        return ResponseEntity.ok(appointmentServiceIml.getAppointmentsByDoctorId(doctorId))
    }

    override fun getAppointmentsByPatientId(patientId: Long): ResponseEntity<List<AppointmentResponseDto>> {
        return ResponseEntity.ok(appointmentServiceIml.getAppointmentsByPatientId(patientId))
    }

    override fun updateAppointment(
        id: Long,
        appointmentCreateDto: AppointmentCreateDto
    ): ResponseEntity<AppointmentResponseDto> {
        return ResponseEntity.ok(appointmentServiceIml.updateAppointment(id, appointmentCreateDto))
    }
}