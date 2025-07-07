package org.example.hospital.core.patient.web

import org.example.hospital.core.configuration.LocalToOffsetDateTimeConverter
import org.example.hospital.core.patient.storage.Patient
import org.example.hospital.generated.api.model.PatientCreateDto
import org.example.hospital.generated.api.model.PatientResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.mapstruct.MappingTarget
import org.springframework.beans.factory.annotation.Autowired
import java.time.Clock
import java.time.LocalDateTime

@Mapper(componentModel = SPRING, uses = [LocalToOffsetDateTimeConverter::class])
abstract class PatientMapper{
    @Autowired
    protected lateinit var clock: Clock

    abstract fun toDto(patient: Patient): PatientResponseDto

    @Mapping(target = "createdAt", expression = "java(currentTime())")
    abstract fun toEntity(dto: PatientCreateDto): Patient

    abstract fun updateEntityFromDto(dto: PatientCreateDto, @MappingTarget patient: Patient)

    protected fun currentTime(): LocalDateTime {
        return LocalDateTime.now(clock)
    }
}