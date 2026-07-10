package normalizer

import misc.IdGenerator
import model.domain.prime.PrimeSet
import model.domain.prime.PrimeType
import model.raw.RawPrimeSet

class PrimeSetNormalizer {

    fun normalize(
        rawPrimeSets: List<RawPrimeSet>
    ): List<PrimeSet> {
        return rawPrimeSets.map(::normalize)
    }

    private fun normalize(
        raw: RawPrimeSet
    ): PrimeSet {
        return PrimeSet(
            id = IdGenerator.generateId(raw.name),
            name = raw.name,
            type = PrimeType.valueOf(raw.type.name),

            image = raw.imageUrl,

            components = raw.components ?: listOf()
        )
    }
}