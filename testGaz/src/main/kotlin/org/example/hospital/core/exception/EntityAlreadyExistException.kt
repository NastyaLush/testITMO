package org.example.hospital.core.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class EntityAlreadyExistException(message: String): AbstractException(message)