package consistency

import consistency.model.ConsistencyContext
import json.JsonManager
import model.domain.FileSource
import pipeline.Pipeline

class ConsistencyPipeline : Pipeline {

    override fun run() {

        println("=== Consistency Pipeline ===")

        val context = loadContext()

        ConsistencyValidator().validate(context)

        println("Consistency validation completed.")

    }

    private fun loadContext(): ConsistencyContext {

        return ConsistencyContext(
            relics = JsonManager.load(FileSource.RELICS),
            primeSets = JsonManager.load(FileSource.PRIME_SETS),
            primeCollections = JsonManager.load(FileSource.PRIME_COLLECTIONS),
            manifest = JsonManager.load(FileSource.MANIFEST)
        )
    }
}