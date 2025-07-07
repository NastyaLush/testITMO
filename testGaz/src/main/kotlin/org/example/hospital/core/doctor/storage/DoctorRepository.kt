package org.example.hospital.core.doctor.storage

import org.springframework.data.repository.CrudRepository

interface DoctorRepository : CrudRepository<Doctor, Long>
{
    override fun findAll(): List<Doctor>

}