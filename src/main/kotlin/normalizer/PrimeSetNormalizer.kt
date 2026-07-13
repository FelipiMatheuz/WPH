package normalizer

import logging.Logger
import misc.IdGenerator
import model.domain.FileSource
import model.domain.prime.PrimeSet
import model.raw.ValidatedPrimeSet

class PrimeSetNormalizer {

    fun normalize(
        rawPrimeSets: List<ValidatedPrimeSet>
    ): List<PrimeSet> {
        Logger.info(FileSource.PRIME_SETS.logName, "Normalizing collected data...")
        Logger.info(FileSource.PRIME_SETS.logName, "Added ${rawPrimeSets.size} prime set(s)")
        return rawPrimeSets.map(::normalize)
    }

    private fun normalize(raw: ValidatedPrimeSet): PrimeSet {
        return PrimeSet(
            id = IdGenerator.generateId(raw.rawSet.name),
            name = raw.rawSet.name,
            type = raw.rawSet.type,
            image = raw.rawSet.imageUrl,
            components = raw.components
        )
    }
}