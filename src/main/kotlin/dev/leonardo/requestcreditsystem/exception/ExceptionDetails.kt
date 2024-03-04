package dev.leonardo.requestcreditsystem.exception

import java.time.LocalDateTime

data class ExceptionDetails(
    val title: String,
    val status: Int,
    val timestamp: LocalDateTime,
    val exception: String,
    val detail: MutableMap<String, String?>
)
