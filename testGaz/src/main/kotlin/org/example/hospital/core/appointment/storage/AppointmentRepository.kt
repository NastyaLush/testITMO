package org.example.hospital.core.appointment.storage

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.time.OffsetDateTime

interface AppointmentRepository : CrudRepository<Appointment, Long> {

    @Query(
        """
            SELECT a.started_at, a.finished_at, p.email, d.name as doctor_name
            FROM appointments a
            JOIN patients p ON a.patient = p.id
            JOIN doctors d ON a.doctor = d.id
            WHERE date_trunc('second', a.started_at) = date_trunc('second', :now)
            AND a.status = 'BOOKED'
        """
    )
    fun findByDate(now: OffsetDateTime): List<AppointmentWithPersonEmail>

    fun findByDoctor(doctorId: Long): List<Appointment>

    fun findByPatient(patientId: Long): List<Appointment>

    @Query(
        """
        SELECT NOT EXISTS(
        SELECT 1 from appointments where doctor=:doctorId and started_at= :now and status='BOOKED'
        )
    """
    )
    fun isDoctorFree(now: OffsetDateTime, doctorId: Long): Boolean


}