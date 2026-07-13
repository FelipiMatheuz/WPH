package misc

import consistency.ConsistencyPipeline
import pipeline.*

object PipelineRegistry {

    private val pipelines = mapOf(

        "relics" to { RelicPipeline() },

        "prime_sets" to { PrimeSetPipeline() },

        "prime_collections" to { PrimeCollectionPipeline() },

        "manifest" to { ManifestPipeline() },

        "consistency" to { ConsistencyPipeline() }
    )

    fun find(name: String): Pipeline? =
        pipelines[name]?.invoke()
}