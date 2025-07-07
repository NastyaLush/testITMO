package org.example.notifier.controller

import org.example.notifier.dto.NotificationRequest
import org.example.notifier.service.NotifyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notify")
class NotifyController(
    private val notifyService: NotifyService
) {

    @PostMapping
    fun notify(@RequestBody request: NotificationRequest) {
        notifyService.notify(request)
    }
}