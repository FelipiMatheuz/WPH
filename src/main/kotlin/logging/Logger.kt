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

            event.metadata.forEach { (key, value) ->
                append("\n")
                append("└──> ")
                append(key)
                append(": ")
                append(value)
            }
        }

    fun info(
        pipeline: String,
        message: String,
        metadata: Map<String, String> = emptyMap()
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
        metadata: Map<String, String> = emptyMap()
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
        metadata: Map<String, String> = emptyMap()
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