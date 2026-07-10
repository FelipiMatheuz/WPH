package misc

import extractor.PrimeSetDetailsExtractor
import kotlinx.serialization.json.Json
import model.domain.FileSource
import model.domain.prime.PrimeSet
import model.raw.PrimeSetSyncResult
import model.raw.RawPrimeSet
import remote.HtmlDownloader
import java.io.File

class PrimeSetSyncService {

    fun sync(extracted: List<RawPrimeSet>): PrimeSetSyncResult {

        val ignored = IgnoredPrimeSets.load()

        val output = File("data/${FileSource.PRIME_SETS.path}")

        if (!output.exists()) {
            println("Downloading all Prime Sets")
            return PrimeSetSyncResult(listOf(), extracted.filterNot { it.name in ignored })
        }

        val existing = Json.decodeFromString<List<PrimeSet>>(
            output.readText()
        )

        val existingIds =
            existing
                .map { it.id }
                .toSet()

        val filteredPrimeSet = extracted
            .filterNot { it.name in ignored }
            .filterNot { IdGenerator.generateId(it.name) in existingIds }

        filteredPrimeSet.forEach {
            val detailsDocument =
                HtmlDownloader().download(it.pageUrl)

            val components = PrimeSetDetailsExtractor(it.name).extract(document = detailsDocument)
            it.components = components
        }


        return PrimeSetSyncResult(existing, filteredPrimeSet)
    }
}