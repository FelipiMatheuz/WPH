package consistency

import consistency.model.ConsistencyContext
import logging.Logger
import manager.FileManager
import model.domain.FileSource
import pipeline.Pipeline

class ConsistencyPipeline : Pipeline {

    override fun run() {

        Logger.pipelineSection("Consistency")

        val context = loadContext()

        ConsistencyValidator().validate(context)

        Logger.info("Validation completed")
        Logger.pipelineSuccess()

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