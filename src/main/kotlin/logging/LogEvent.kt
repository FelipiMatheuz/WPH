package logging

import java.time.Instant

data class LogEvent(
    val timestamp: Instant = Instant.now(),
    val level: LogLevel,
    val pipeline: String,
    val message: String,
    val metadata: Map<String, String> = emptyMap()
)