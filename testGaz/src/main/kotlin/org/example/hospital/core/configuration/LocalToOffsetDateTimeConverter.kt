package org.example.hospital.core.configuration

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Component
class LocalToOffsetDateTimeConverter(
    private val clock: Clock
) :
    Converter<LocalDateTime, OffsetDateTime> {

    override fun convert(source: LocalDateTime): OffsetDateTime {
        return source.atZone(clock.zone).toOffsetDateTime()
    }
}