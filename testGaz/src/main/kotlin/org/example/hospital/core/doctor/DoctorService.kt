package org.example.hospital.core.doctor

import org.example.hospital.generated.api.model.DoctorCreateDto
import org.example.hospital.generated.api.model.DoctorResponseDto

interface DoctorService {

    fun getAllDoctors(): List<DoctorResponseDto>

    fun createDoctor(doctorCreateDto: DoctorCreateDto): DoctorResponseDto

    fun deleteDoctor(id: Long)

    fun getDoctorById(id: Long): DoctorResponseDto

    fun updateDoctor(id: Long, doctorCreateDto: DoctorCreateDto): DoctorResponseDto
}