package logging

object Logger {

    private const val BOLD = "\u001b[1m"
    private const val GREEN = "\u001b[32m"
    private const val YELLOW = "\u001b[33m"
    private const val CYAN = "\u001b[36m"
    private const val RESET = "\u001b[0m"

    private fun log(event: LogEvent): String =
        buildString {

            append(event.timestamp)
            append(" [")
            append(event.level)
            append("] ")

            if (event.source != null) {
                append(event.source)
                append(": ")
            }

            append(event.message)

            event.metadata.forEach { log ->
                append("\n")
                append("    ► ")
                append(log.key)
                append(" -> ")
                append(log.message)
            }
        }

    fun info(
        message: String,
        source: String? = null,
        metadata: List<LogMetadata> = listOf()
    ) =
        println(
            log(
                LogEvent(
                    level = LogLevel.INFO,
                    source = source,
                    message = message,
                    metadata = metadata
                )
            )
        )

    fun warn(
        message: String,
        metadata: List<LogMetadata> = listOf()
    ) =
        println(
            "$YELLOW${
                log(
                    LogEvent(
                        level = LogLevel.WARN,
                        source = null,
                        message = message,
                        metadata = metadata
                    )
                )
            }$RESET"
        )

    fun error(
        message: String,
        source: String? = null,
        metadata: List<LogMetadata> = listOf()
    ): Nothing =
        throw PipelineException(
            log(
                LogEvent(
                    level = LogLevel.ERROR,
                    source = source,
                    message = message,
                    metadata = metadata
                )
            )
        )

    fun pipelineSection(pipelineName: String) {
        info("${BOLD}${CYAN}>>> $pipelineName pipeline started$RESET")
    }

    fun pipelineSuccess() {
        info("${BOLD}${GREEN}✅ Pipeline finished successfully$RESET")
    }
}