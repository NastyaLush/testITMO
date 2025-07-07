package org.example.hospital.core.appointment

import org.example.hospital.generated.api.model.AppointmentCreateDto
import org.example.hospital.generated.api.model.AppointmentResponseDto


interface AppointmentService {


    fun createAppointment(dto: AppointmentCreateDto): AppointmentResponseDto

    fun updateAppointment(id: Long, dto: AppointmentCreateDto): AppointmentResponseDto

    fun getAppointmentById(id: Long): AppointmentResponseDto

    fun getAppointmentsByDoctorId(doctorId: Long): List<AppointmentResponseDto>

    fun getAppointmentsByPatientId(patientId: Long): List<AppointmentResponseDto>

    fun deleteAppointment(id: Long)

}