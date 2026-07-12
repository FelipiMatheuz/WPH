package misc

import manager.FileManager
import kotlinx.serialization.json.Json
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.raw.RawPrimeCollection

class PrimeCollectionSyncService {

    fun collectionExists(extracted: RawPrimeCollection): Boolean {

        val output = FileManager.dataFile(FileSource.PRIME_COLLECTIONS)

        if (!output.exists()) {
            println("prime_collections.json file does not exists.")
            return false
        }

        val existing = Json.decodeFromString<List<PrimeCollection>>(
            output.readText()
        )

        val existingIds =
            existing
                .map { it.id }
                .toSet()

        return IdGenerator.generateCollection(extracted.name) in existingIds
    }
}