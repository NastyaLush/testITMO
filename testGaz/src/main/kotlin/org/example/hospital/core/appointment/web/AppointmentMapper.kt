package org.example.hospital.core.appointment.web

import org.example.hospital.core.appointment.storage.Appointment
import org.example.hospital.generated.api.model.AppointmentCreateDto
import org.example.hospital.generated.api.model.AppointmentResponseDto
import org.example.hospital.generated.api.model.Status
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel.SPRING

@Mapper(
    componentModel = SPRING
)
interface AppointmentMapper {

    fun toDto(appointment: Appointment): AppointmentResponseDto

    fun toEntity(dto: AppointmentCreateDto): Appointment

    fun apiStatusToEntityStatus(status: Status): Appointment.Status
}