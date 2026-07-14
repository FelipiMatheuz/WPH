import misc.PipelineRegistry
import logging.Logger

fun main(args: Array<String>) {

    require(args.isNotEmpty()) {
        "Pipeline name is required."
    }

    val pipelineName = args.first()

    val pipeline =
        PipelineRegistry.find(pipelineName)
            ?: Logger.error("Unknown pipeline '$pipelineName'.")

    pipeline.run()
}
