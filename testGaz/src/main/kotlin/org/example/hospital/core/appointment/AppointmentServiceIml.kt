package org.example.hospital.core.appointment

import org.example.hospital.core.appointment.storage.Appointment
import org.example.hospital.core.appointment.storage.AppointmentRepository
import org.example.hospital.core.appointment.web.AppointmentMapper
import org.example.hospital.core.exception.BadRequestException
import org.example.hospital.core.exception.EntityAlreadyExistException
import org.example.hospital.core.exception.EntityNotFoundException
import org.example.hospital.generated.api.model.AppointmentCreateDto
import org.example.hospital.generated.api.model.AppointmentResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppointmentServiceIml(
    private val appointmentRepository: AppointmentRepository,
    private val appointmentMapper: AppointmentMapper
) : AppointmentService {

    @Transactional
    override fun createAppointment(dto: AppointmentCreateDto): AppointmentResponseDto {
        val appointment = appointmentMapper.toEntity(dto)

        if (!appointmentRepository.isDoctorFree(appointment.startedAt, appointment.doctor)) {
            throw EntityAlreadyExistException("Slot is busy")
        }

        validateAppointment(appointment)

        return appointmentMapper.toDto(appointmentRepository.save(appointment))
    }

    @Transactional
    override fun updateAppointment(id: Long, dto: AppointmentCreateDto): AppointmentResponseDto {
        val existing = appointmentRepository.findById(id)
            .orElseThrow { RuntimeException("Appointment not found") }

        val updated = existing.apply {
            this.id = existing.id
            this.doctor = dto.doctor
            this.patient = dto.patient
            this.startedAt = dto.startedAt
            this.finishedAt = dto.finishedAt
            this.status = appointmentMapper.apiStatusToEntityStatus(dto.status)
        }

        validateAppointment(updated)

        return appointmentMapper.toDto(appointmentRepository.save(updated))
    }

    override fun getAppointmentById(id: Long): AppointmentResponseDto {
        val appointment = appointmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Appointment not found") }

        return appointmentMapper.toDto(appointment)
    }

    override fun getAppointmentsByDoctorId(doctorId: Long): List<AppointmentResponseDto> {
        return appointmentRepository.findByDoctor(doctorId).map(appointmentMapper::toDto)
    }

    override fun getAppointmentsByPatientId(patientId: Long): List<AppointmentResponseDto> {
        return appointmentRepository.findByPatient(patientId).map(appointmentMapper::toDto)
    }

    @Transactional
    override fun deleteAppointment(id: Long) {
        if (!appointmentRepository.existsById(id)) {
            throw EntityNotFoundException("Appointment not found")
        }

        appointmentRepository.deleteById(id)
    }

    private fun validateAppointment(appointment: Appointment) {
        if (
            appointment.status == Appointment.Status.BOOKED && appointment.patient == null
            || appointment.status == Appointment.Status.FREE && appointment.patient != null
        ) {
            throw BadRequestException(
                "When appointment is booked it must have a person." +
                        "\nWhen appointment is free it must not have a person."
            )
        }
    }
}