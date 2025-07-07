package org.example.hospital.core.patient.storage

import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface PatientRepository : CrudRepository<Patient, Long> {

    fun findPatientByEmail(email: String):Optional<Patient>

}