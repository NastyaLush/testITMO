package org.example.hospital

import org.springframework.context.annotation.Bean
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ClockConfiguration {
    @Bean
    fun fixedTestClock(): Clock {
        return Clock.fixed(
            Instant.now().minusSeconds(60L).truncatedTo(ChronoUnit.SECONDS),
            ZoneId.systemDefault()
        );
    }
}