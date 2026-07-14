package datasync

import extractor.PrimeSetDetailsExtractor
import kotlinx.serialization.json.Json
import logging.LogMetadata
import logging.Logger
import manager.FileManager
import misc.IdGenerator
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

        val existingIds = existing.map { it.id }.toSet()

        val extractedWithDetails = extracted
            .filterNot { IdGenerator.generateId(it.name) in existingIds }
            .map { raw -> fetchDetails(raw) }

        Logger.info(
            "Synchronization finished", "Sync Service",
            listOf(
                LogMetadata("Existing", existing.size.toString()),
                LogMetadata("New", extractedWithDetails.size.toString())
            )
        )
        return PrimeSetSyncResult(existing, extractedWithDetails)
    }

    private fun fetchDetails(rawPrimeSet: RawPrimeSet): RawPrimeSetWithComponents {
        val detailsDocument =
            HtmlDownloader().download(rawPrimeSet.pageUrl)

        val components = PrimeSetDetailsExtractor(rawPrimeSet.name).extract(detailsDocument)

        return components?.let { RawPrimeSetWithComponents(rawPrimeSet, it) } ?: Logger.error(
            rawPrimeSet.name,
            "No components found for the prime set"
        )
    }
}