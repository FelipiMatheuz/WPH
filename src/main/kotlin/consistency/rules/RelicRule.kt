package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource

class RelicRule : ConsistencyRule {
    override fun validate(context: ConsistencyContext): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating relic list...")

        val validRarities = setOf(
            "Common",
            "Uncommon",
            "Rare"
        )
        val relics = context.relics
        val errors: MutableList<ValidationError> = mutableListOf()

        relics.forEach { relic ->

            relic.drops.forEach { drop ->

                if (drop.id.isBlank()) {
                    errors.add(
                        ValidationError(
                            FileSource.RELICS,
                            "Relic '${relic.id}' contains an empty drop."
                        )
                    )
                }

                if (drop.rarity !in validRarities) {
                    errors.add(
                        ValidationError(
                            FileSource.RELICS,
                            "Invalid rarity '${drop.rarity}' in relic '${relic.id}'."
                        )
                    )
                }

            }

        }
        return errors
    }
}