package misc

import extractor.PrimeSetDetailsExtractor
import manager.FileManager
import kotlinx.serialization.json.Json
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimeSet
import model.raw.PrimeSetSyncResult
import model.raw.RawPrimeSet
import model.raw.RawPrimeSetWithComponents
import remote.HtmlDownloader

class PrimeSetSyncService {

    fun sync(extracted: List<RawPrimeSet>): PrimeSetSyncResult {

        Logger.info(FileSource.PRIME_SETS.logName, "Checking new prime sets...")
        val output = FileManager.dataFile(FileSource.PRIME_SETS)

        val existing = if (!output.exists()) {
            Logger.warn(FileSource.PRIME_SETS.logName, "No prime_sets.json file found.")
            listOf()
        } else {
            Json.decodeFromString<List<PrimeSet>>(output.readText())
        }

        val ignored = IgnoredPrimeSets.load()
        val existingIds = existing.map { it.id }.toSet()

        val extractedWithDetails = extracted
            .filterNot { it.name in ignored }
            .filterNot { IdGenerator.generateId(it.name) in existingIds }
            .map { raw -> fetchDetails(raw) }

        val newPrimeSets = IgnoredPrimeSets.update(extractedWithDetails)

        return PrimeSetSyncResult(existing, newPrimeSets)
    }

    private fun fetchDetails(raw: RawPrimeSet): RawPrimeSetWithComponents {
        val detailsDocument =
            HtmlDownloader().download(raw.pageUrl)

        val components = PrimeSetDetailsExtractor(raw.name).extract(document = detailsDocument)

        return RawPrimeSetWithComponents(raw, components)
    }
}