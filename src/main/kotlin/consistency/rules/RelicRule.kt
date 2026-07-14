package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource

class RelicRule : ConsistencyRule {
    override fun validate(context: ConsistencyContext): List<LogMetadata> {

        Logger.info("Validating relic list...")

        val validRarities = setOf(
            "Common",
            "Uncommon",
            "Rare"
        )
        val relics = context.relics
        val errors: MutableList<LogMetadata> = mutableListOf()

        relics.forEach { relic ->

            relic.drops.forEach { drop ->

                if (drop.id.isBlank()) {
                    errors.add(
                        LogMetadata(
                            FileSource.RELICS.logName,
                            "Relic '${relic.id}' contains an empty drop."
                        )
                    )
                }

                if (drop.rarity !in validRarities) {
                    errors.add(
                        LogMetadata(
                            FileSource.RELICS.logName,
                            "Invalid rarity '${drop.rarity}' in relic '${relic.id}'."
                        )
                    )
                }

            }

        }
        return errors
    }
}