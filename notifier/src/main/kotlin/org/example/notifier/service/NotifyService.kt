package org.example.notifier.service

import org.example.notifier.dto.NotificationRequest

interface NotifyService {

    fun notify(notifyRequest: NotificationRequest)
}