package normalizer

import kotlinx.serialization.json.Json
import misc.IdGenerator
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.raw.RawPrimeCollection
import java.io.File

class PrimeCollectionNormalizer {

    fun normalize(raw: RawPrimeCollection): List<PrimeCollection> {
        val output = File("data/${FileSource.PRIME_COLLECTIONS.path}")
        val collections =
            if (output.exists()) {
                Json.decodeFromString<MutableList<PrimeCollection>>(
                    output.readText()
                )
            } else {
                mutableListOf()
            }
        collections.add(normalizeCollection(raw))

        return collections.sortedByDescending { it.released }
    }

    private fun normalizeCollection(raw: RawPrimeCollection): PrimeCollection =
        PrimeCollection(
            id = IdGenerator.generateCollection(raw.name),
            name = raw.name.trim(),
            released = raw.released,
            promoImage = raw.promoUrl.trim(),
            primeSets = raw.items.reversed().map { IdGenerator.generateId(it) }
        )
}