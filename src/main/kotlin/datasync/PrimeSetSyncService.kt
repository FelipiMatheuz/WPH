package datasync

import extractor.PrimeSetDetailsExtractor
import manager.FileManager
import kotlinx.serialization.json.Json
import logging.LogMetadata
import logging.Logger
import misc.IdGenerator
import misc.IgnoredPrimeSets
import model.domain.FileSource
import model.domain.prime.PrimeSet
import model.raw.PrimeSetSyncResult
import model.raw.RawPrimeSet
import model.raw.RawPrimeSetWithComponents
import remote.HtmlDownloader

class PrimeSetSyncService {

    fun sync(extracted: List<RawPrimeSet>): PrimeSetSyncResult {

        Logger.info("Checking new prime sets...")
        val output = FileManager.dataFile(FileSource.PRIME_SETS)

        val existing = if (!output.exists()) {
            Logger.warn(
                "File ${FileSource.PRIME_SETS.path} not found.",
                listOf(LogMetadata("Path", output.path))
            )
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

        Logger.info(
            "Synchronization finished", "Sync Service",
            listOf(
                LogMetadata("New", newPrimeSets.size.toString()),
                LogMetadata("Existing", existing.size.toString()),
                LogMetadata("Ignored", IgnoredPrimeSets.load().toString())
            )
        )
        return PrimeSetSyncResult(existing, newPrimeSets)
    }

    private fun fetchDetails(raw: RawPrimeSet): RawPrimeSetWithComponents {
        val detailsDocument =
            HtmlDownloader().download(raw.pageUrl)

        val components = PrimeSetDetailsExtractor(raw.name).extract(detailsDocument)

        return RawPrimeSetWithComponents(raw, components)
    }
}