package org.example.hospital.notifier

import org.example.hospital.core.appointment.storage.AppointmentRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.OffsetDateTime

@Service
class AppointmentReminderScheduler(
    private val appointmentRepository: AppointmentRepository,
    private val notificationClient: NotificationServiceClient,
    private val clock: Clock
) {

    @Scheduled(cron ="\${notifier.cron:0 * * * * ?}")
    fun sendAppointmentReminders() {

        val now = OffsetDateTime.now(clock)

        val twoHourAppointments = appointmentRepository.findByDate(now.plusHours(2))
        val twentyFourHourAppointments = appointmentRepository.findByDate(now.plusHours(24))


        twoHourAppointments.forEach {
            notificationClient.notify(
                NotificationRequest(
                    "Reminder: Your appointment starts at ${it.startedAt} and finishes at ${it.finishedAt} with ${it.doctorName}",
                    it.email
                )
            )
        }

        twentyFourHourAppointments.forEach {
            notificationClient.notify(
                NotificationRequest(
                    "Reminder: Your appointment starts at ${it.startedAt} and finishes at ${it.finishedAt} with ${it.doctorName}",
                    it.email
                )
            )
        }
    }
}