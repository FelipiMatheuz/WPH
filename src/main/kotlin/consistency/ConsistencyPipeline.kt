package consistency

import consistency.model.ConsistencyContext
import kotlinx.serialization.json.Json
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.domain.prime.PrimeSet
import model.domain.relic.Relic
import model.raw.Manifest
import pipeline.Pipeline
import java.io.File

class ConsistencyPipeline : Pipeline {

    override fun run() {

        println("=== Consistency Pipeline ===")

        val context = loadContext()

        ConsistencyValidator().validate(context)

        println("Consistency validation completed.")

    }

    private fun loadContext(): ConsistencyContext {

        return ConsistencyContext(
            relics = readRelicJson(FileSource.RELICS.path),
            primeSets = readPrimeSetJson(FileSource.PRIME_SETS.path),
            primeCollections = readPrimeCollectionJson(FileSource.PRIME_COLLECTIONS.path),
            manifest = readManifestJson(FileSource.MANIFEST.path)
        )
    }

    private fun readRelicJson(path: String): List<Relic> {
        val output = File("data/${path}")

        if (!output.exists()) {
            println("$path file does not exists.")
        }

        return Json.decodeFromString<List<Relic>>(
            output.readText()
        )
    }

    private fun readPrimeSetJson(path: String): List<PrimeSet> {
        val output = File("data/${path}")

        if (!output.exists()) {
            println("$path file does not exists.")
        }

        return Json.decodeFromString<List<PrimeSet>>(
            output.readText()
        )
    }

    private fun readPrimeCollectionJson(path: String): List<PrimeCollection> {
        val output = File("data/${path}")

        if (!output.exists()) {
            println("$path file does not exists.")
        }

        return Json.decodeFromString<List<PrimeCollection>>(
            output.readText()
        )
    }

    private fun readManifestJson(path: String): Manifest {
        val output = File("data/${path}")

        if (!output.exists()) {
            println("$path file does not exists.")
        }

        return Json.decodeFromString<Manifest>(
            output.readText()
        )
    }

}