package misc

import pipeline.ManifestPipeline
import pipeline.Pipeline
import pipeline.PrimeSetPipeline
import pipeline.RelicPipeline

object PipelineRegistry {

    private val pipelines = mapOf(

        "relics" to RelicPipeline(),

        "prime_sets" to PrimeSetPipeline(),

        "manifest" to ManifestPipeline()

    )

    fun find(name: String): Pipeline? =
        pipelines[name]
}