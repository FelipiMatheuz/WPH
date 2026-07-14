package normalizer

import manager.FileManager
import kotlinx.serialization.json.Json
import logging.LogMetadata
import logging.Logger
import misc.IdGenerator
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.raw.RawPrimeCollection

class PrimeCollectionNormalizer {

    fun normalize(raw: RawPrimeCollection, imageUrl: String): List<PrimeCollection> {

        Logger.info("Normalizing collected data...")
        val output = FileManager.dataFile(FileSource.PRIME_COLLECTIONS)
        val collections =
            if (output.exists()) {
                Json.decodeFromString<MutableList<PrimeCollection>>(
                    output.readText()
                )
            } else {
                mutableListOf()
            }
        collections.add(normalizeCollection(raw, imageUrl))
        Logger.info(
            "Added a new collection!", null,
            listOf(
                LogMetadata("Number of prime collections", collections.size.toString())
            )
        )
        return collections.sortedByDescending { it.released }
    }

    private fun normalizeCollection(raw: RawPrimeCollection, imageUrl: String): PrimeCollection =
        PrimeCollection(
            id = IdGenerator.generateCollectionId(raw.name),
            name = raw.name.trim(),
            released = raw.released,
            promoImage = imageUrl.trim(),
            primeSets = raw.items.reversed().map { IdGenerator.generateId(it) }
        )
}