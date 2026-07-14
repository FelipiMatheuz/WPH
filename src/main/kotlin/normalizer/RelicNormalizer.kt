package normalizer

import logging.Logger
import misc.IdGenerator
import model.domain.relic.AcquisitionSource
import model.domain.relic.Drop
import model.domain.relic.Relic
import model.raw.RawRelic
import model.raw.RawRelicSource

class RelicNormalizer {

    fun normalize(
        rawRelics: List<RawRelic>,
        rawRelicSource: List<RawRelicSource>
    ): List<Relic> {

        Logger.info("Normalizing collected data...")
        val sourceMap = rawRelicSource.associateBy { it.relicId }
        val normalizedRelics = rawRelics.map { raw ->

            val id = IdGenerator.generateId("${raw.era} ${raw.name}")
            val source = sourceMap[id]?.source ?: AcquisitionSource.VAULT

            Relic(
                id = id,
                name = raw.name,
                era = raw.era,
                source = source,
                drops = raw.drops.map {
                    Drop(
                        id = IdGenerator.generateId(it.itemName),
                        rarity = it.rarity
                    )
                }
            )
        }
        Logger.info("Normalized ${normalizedRelics.size} relic(s) successfully")
        return normalizedRelics
    }

}
