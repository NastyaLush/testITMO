package org.example.notifier.service.impl

import jakarta.annotation.PostConstruct
import org.example.notifier.configuration.FileConfig
import org.example.notifier.dto.NotificationRequest
import org.example.notifier.service.NotifyService
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class NotifyServiceImpl(
    private val fileConfig: FileConfig
) : NotifyService {

    companion object {
        private const val DELIMITER = "\n"
    }

    override fun notify(notifyRequest: NotificationRequest) {
        val logEntry = getLogEntry(notifyRequest)
        val file = File(fileConfig.path)
        file.parentFile?.mkdirs()
        file.appendText(logEntry + DELIMITER)
    }

    private fun getLogEntry(notifyRequest: NotificationRequest): String {
        return """
            Time: ${LocalDateTime.now()}
            Email: ${notifyRequest.email}
            Message: ${notifyRequest.message}
            --------------
        """.trimIndent()
    }
}