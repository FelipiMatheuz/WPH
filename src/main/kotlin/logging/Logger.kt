package logging

object Logger {

    private fun log(event: LogEvent): String =
        buildString {

            append("[")
            append(event.level)
            append("] ")

            append(event.pipeline)
            append(": ")

            append(event.message)

            event.metadata.forEach { message ->
                append("\n")
                append("    ► ")
                append(message)
            }
        }

    fun info(
        pipeline: String,
        message: String,
        metadata: List<String> = listOf()
    ) =
        println(
            log(
                LogEvent(
                    level = LogLevel.INFO,
                    pipeline = pipeline,
                    message = message,
                    metadata = metadata
                )
            )
        )

    fun warn(
        pipeline: String,
        message: String,
        metadata: List<String> = listOf()
    ) =
        println(
            log(
                LogEvent(
                    level = LogLevel.WARN,
                    pipeline = pipeline,
                    message = message,
                    metadata = metadata
                )
            )
        )

    fun error(
        pipeline: String,
        message: String,
        metadata: List<String> = listOf()
    ): Nothing =
        throw PipelineException(
            log(
                LogEvent(
                    level = LogLevel.ERROR,
                    pipeline = pipeline,
                    message = message,
                    metadata = metadata
                )
            )
        )
}