package org.example.hospital.core.doctor.web

import org.example.hospital.core.doctor.DoctorService
import org.example.hospital.generated.api.DoctorManagementApi
import org.example.hospital.generated.api.model.DoctorCreateDto
import org.example.hospital.generated.api.model.DoctorResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DoctorController(
    private val doctorService: DoctorService
) : DoctorManagementApi {

    override fun getAllDoctors(): ResponseEntity<List<DoctorResponseDto>> {
        val doctors = doctorService.getAllDoctors()
        return ResponseEntity.ok(doctors)
    }

    override fun createDoctor(doctorCreateDto: DoctorCreateDto): ResponseEntity<DoctorResponseDto> {
        val response = doctorService.createDoctor(doctorCreateDto)
        return ResponseEntity.status(201).body(response)
    }

    override fun deleteDoctor(id: Long): ResponseEntity<Unit> {
        doctorService.deleteDoctor(id)
        return ResponseEntity.noContent().build()
    }

    override fun getDoctorById(id: Long): ResponseEntity<DoctorResponseDto> {
        val response = doctorService.getDoctorById(id)
        return ResponseEntity.ok(response)
    }

    override fun updateDoctor(id: Long, doctorCreateDto: DoctorCreateDto): ResponseEntity<DoctorResponseDto> {
        val response = doctorService.updateDoctor(id, doctorCreateDto)
        return ResponseEntity.ok(response)
    }
}