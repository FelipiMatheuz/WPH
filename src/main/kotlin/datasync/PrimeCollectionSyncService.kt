package datasync

import manager.FileManager
import kotlinx.serialization.json.Json
import logging.LogMetadata
import logging.Logger
import misc.IdGenerator
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.raw.RawPrimeCollection

class PrimeCollectionSyncService {

    fun isSynced(extracted: RawPrimeCollection): Boolean {

        val output = FileManager.dataFile(FileSource.PRIME_COLLECTIONS)

        if (!output.exists()) {
            Logger.warn(
                "File ${FileSource.PRIME_COLLECTIONS.path} not found.",
                listOf(LogMetadata("path", output.path))
            )
            return false
        }

        val existing = Json.decodeFromString<List<PrimeCollection>>(
            output.readText()
        )

        val existingIds =
            existing
                .map { it.id }
                .toSet()

        return IdGenerator.generateCollectionId(extracted.name) in existingIds
    }
}