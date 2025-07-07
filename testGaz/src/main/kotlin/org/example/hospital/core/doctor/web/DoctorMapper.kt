package org.example.hospital.core.doctor.web

import org.example.hospital.core.configuration.LocalToOffsetDateTimeConverter
import org.example.hospital.core.configuration.OffsetToLocalDateTimeConverter
import org.example.hospital.core.doctor.storage.Doctor
import org.example.hospital.generated.api.model.DoctorCreateDto
import org.example.hospital.generated.api.model.DoctorResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants.ComponentModel.SPRING
import org.springframework.beans.factory.annotation.Autowired
import java.time.Clock
import java.time.LocalDateTime

@Mapper(componentModel = SPRING, uses = [LocalToOffsetDateTimeConverter::class, OffsetToLocalDateTimeConverter::class])
abstract class DoctorMapper {
    @Autowired
    protected lateinit var clock: Clock

    abstract fun toResponseDtoList(doctors: List<Doctor>): List<DoctorResponseDto>

    abstract fun toResponseDto(doctor: Doctor): DoctorResponseDto

    @Mapping(target = "createdAt", expression = "java(currentTime())")
    abstract fun toEntity(doctorCreateDto: DoctorCreateDto): Doctor

    protected fun currentTime(): LocalDateTime {
        return LocalDateTime.now(clock)
    }
}