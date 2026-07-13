package normalizer

import misc.IdGenerator
import model.domain.relic.AcquisitionSource
import model.domain.relic.Drop
import model.domain.relic.Relic
import model.raw.RawRelic
import model.raw.RelicSource
import logging.Logger
import model.domain.FileSource

class RelicNormalizer {

    fun normalize(
        relics: List<RawRelic>,
        rawRelicSource: List<RelicSource>
    ): List<Relic> {

        Logger.info(FileSource.RELICS.logName, "Normalizing collected data...")
        val sourceMap = rawRelicSource.associateBy { it.relicId }
        return relics.map { raw ->

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

    }

}
