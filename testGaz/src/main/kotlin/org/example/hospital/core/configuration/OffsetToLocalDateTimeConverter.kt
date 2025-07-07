package org.example.hospital.core.configuration

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Component
class OffsetToLocalDateTimeConverter :
    Converter<OffsetDateTime, LocalDateTime> {

    override fun convert(source: OffsetDateTime): LocalDateTime {
        return source.toLocalDateTime()
    }
}