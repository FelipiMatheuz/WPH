package consistency

import consistency.model.ConsistencyContext
import manager.FileManager
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
            relics = FileManager.load(FileSource.RELICS),
            primeSets = FileManager.load(FileSource.PRIME_SETS),
            primeCollections = FileManager.load(FileSource.PRIME_COLLECTIONS),
            manifest = FileManager.load(FileSource.MANIFEST)
        )
    }
}