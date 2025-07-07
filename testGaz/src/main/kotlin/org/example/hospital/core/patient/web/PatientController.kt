package org.example.hospital.core.patient.web

import org.example.hospital.core.patient.PatientService
import org.example.hospital.generated.api.PatientManagementApi
import org.example.hospital.generated.api.model.PatientCreateDto
import org.example.hospital.generated.api.model.PatientResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class PatientController(
    private val patientService: PatientService
) : PatientManagementApi {

    override fun createPatient(patientCreateDto: PatientCreateDto): ResponseEntity<PatientResponseDto> {
        val dto = patientService.createPatient(patientCreateDto)
        return ResponseEntity.status(201).body(dto)
    }

    override fun deletePatient(id: Long): ResponseEntity<Unit> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }

    override fun getAllPatients(): ResponseEntity<List<PatientResponseDto>> {
        return ResponseEntity.ok(patientService.getAllPatients())
    }

    override fun getPatientById(id: Long): ResponseEntity<PatientResponseDto> {
        return ResponseEntity.ok(patientService.getPatientById(id))
    }

    override fun updatePatient(id: Long, patientCreateDto: PatientCreateDto): ResponseEntity<PatientResponseDto> {
        return ResponseEntity.ok(patientService.updatePatient(id, patientCreateDto))
    }
}