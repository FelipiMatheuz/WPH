package pipeline

object PipelineRegistry {

    private val pipelines = mapOf(

        "relics" to RelicPipeline(),

        "manifest" to ManifestPipeline()

    )

    fun find(name: String): Pipeline? =
        pipelines[name]
}