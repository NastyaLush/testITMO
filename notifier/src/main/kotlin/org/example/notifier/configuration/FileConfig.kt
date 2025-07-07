package org.example.notifier.configuration

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "notifier-file")
class FileConfig {

    @field:NotBlank(message = "File name must not be blank")
    lateinit var path: String
}