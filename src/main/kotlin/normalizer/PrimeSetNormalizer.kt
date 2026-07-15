package normalizer

import logging.LogMetadata
import logging.Logger
import misc.IdGenerator
import model.domain.prime.PrimeSet
import model.raw.RawPrimeSetWithComponents

class PrimeSetNormalizer {

    fun normalize(
        rawPrimeSets: List<RawPrimeSetWithComponents>
    ): List<PrimeSet> {
        Logger.info("Normalizing collected data...")
        val normalizedPrimeSets = rawPrimeSets.map(::normalize)
        Logger.info(
            "Added ${normalizedPrimeSets.size} prime set(s)!", null,
            normalizedPrimeSets.map { LogMetadata("new ${it.type}", it.name) }
        )
        return normalizedPrimeSets
    }

    private fun normalize(raw: RawPrimeSetWithComponents): PrimeSet {
        return PrimeSet(
            id = IdGenerator.generateId(raw.rawSet.name),
            name = raw.rawSet.name,
            type = raw.rawSet.type,
            image = raw.rawSet.imageUrl,
            components = raw.components
        )
    }
}