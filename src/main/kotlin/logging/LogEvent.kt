package logging

import java.time.Instant

data class LogEvent(
    val timestamp: Instant = Instant.now(),
    val level: LogLevel,
    val source: String?,
    val message: String,
    val metadata: List<LogMetadata>
)