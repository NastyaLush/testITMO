package org.example.hospital

import com.github.tomakehurst.wiremock.client.WireMock.*
import org.example.hospital.generated.api.model.AppointmentResponseDto
import org.example.hospital.generated.api.model.Status
import org.example.hospital.notifier.AppointmentReminderScheduler
import org.example.hospital.notifier.NotificationRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.OffsetDateTime

class SchedulerTest : AbstractTest() {
    @Autowired
    protected lateinit var appointmentReminderScheduler: AppointmentReminderScheduler

    @Test
    fun checkScheduleNotification() {
        createAppointment(Status.FREE, OffsetDateTime.now(clock))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(1))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(2))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(2).plusMinutes(1))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(2).minusMinutes(1))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(3))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(23))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(24))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(24).plusMinutes(1))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(24).minusMinutes(1))
        createAppointment(Status.FREE, OffsetDateTime.now(clock).plusHours(25))


        createAppointment(Status.BOOKED, OffsetDateTime.now(clock))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(1))
        val createAppointment1 = createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(2))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(2).plusMinutes(1))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(2).minusMinutes(1))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(3))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(23))
        val createAppointment3 = createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(24))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(24).plusMinutes(1))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(24).minusMinutes(1))
        createAppointment(Status.BOOKED, OffsetDateTime.now(clock).plusHours(25))

        stubNotifier(createAppointment1)
        stubNotifier(createAppointment3)

        appointmentReminderScheduler.sendAppointmentReminders()

        verifyNotificationSent(createAppointment1)
        verifyNotificationSent(createAppointment3)
    }

    private fun verifyNotificationSent(appointment: AppointmentResponseDto) {

        val email = getPatient(appointment.patient!!).email
        verify(
            postRequestedFor(urlPathEqualTo("/api/v1/notify"))
                .withRequestBody(
                    matchingJsonPath("$.email", equalTo(email))
                )
        )
    }

    private fun stubNotifier(createAppointment1: AppointmentResponseDto) {
        val patient = getPatient(createAppointment1.patient!!)

        val doctor = getDoctor(createAppointment1.doctor)

        stubFor(
            post(urlPathEqualTo("/api/v1/notify"))
                .withRequestBody(
                    equalToJson(
                        objectMapper.writeValueAsString(
                            NotificationRequest(
                                """Reminder: Your appointment starts at ${createAppointment1.startedAt} and finishes at ${createAppointment1.finishedAt} with ${doctor.name}""".trimIndent(),
                                patient.email
                            )
                        )
                    )
                )
                .willReturn(
                    ok()
                )
        )
    }
}