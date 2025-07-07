package org.example.hospital.core.patient

import org.example.hospital.core.patient.storage.PatientRepository
import org.example.hospital.core.patient.web.PatientMapper
import org.example.hospital.core.exception.EntityAlreadyExistException
import org.example.hospital.core.exception.EntityNotFoundException
import org.example.hospital.generated.api.model.PatientCreateDto
import org.example.hospital.generated.api.model.PatientResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PatientService(
    private val patientRepository: PatientRepository,
    private val patientMapper: PatientMapper
) {

    @Transactional
    fun createPatient(patientCreateDto: PatientCreateDto): PatientResponseDto {
        if (patientRepository.findPatientByEmail(patientCreateDto.email).isPresent) {
            throw EntityAlreadyExistException("Patient with this email already exist")
        }

        val patient = patientMapper.toEntity(patientCreateDto)

        return patientMapper.toDto(patientRepository.save(patient))
    }

    @Transactional
    fun updatePatient(id: Long, patientCreateDto: PatientCreateDto): PatientResponseDto {
        val existingPatient = patientRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Patient not found") }

        patientMapper.updateEntityFromDto(patientCreateDto, existingPatient)

        return patientMapper.toDto(patientRepository.save(existingPatient))
    }

    fun getPatientById(id: Long): PatientResponseDto {
        val patient = patientRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Patient not found") }

        return patientMapper.toDto(patient)
    }

    fun getAllPatients(): List<PatientResponseDto> {
        return patientRepository.findAll().map(patientMapper::toDto)
    }

    @Transactional
    fun deletePatient(id: Long) {
        if (!patientRepository.existsById(id)) {
            throw EntityNotFoundException("Patient not found")
        }

        patientRepository.deleteById(id)
    }
}