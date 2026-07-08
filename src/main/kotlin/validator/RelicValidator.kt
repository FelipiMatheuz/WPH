package validator

import model.domain.Relic
import org.jsoup.helper.ValidationException

class RelicValidator {

    val errors = mutableListOf<String>()
    fun validate(data: List<Relic>) {

        println("Validating relic data...")
        validateUniqueIds(data)
        validateDrops(data)

        if (errors.isNotEmpty()) {
            throw ValidationException(
                buildString {
                    appendLine("Validation failed:")
                    errors.forEach { appendLine("- $it") }
                }
            )
        }
    }

    private fun validateUniqueIds(
        relics: List<Relic>
    ) {

        val duplicated = relics
            .groupBy { it.id }
            .filterValues { it.size > 1 }

        if (duplicated.isNotEmpty()) {
            errors.add("Duplicated relic ids: ${duplicated.keys.joinToString()}")
        }
    }

    private fun validateDrops(
        relics: List<Relic>
    ) {
        val validRarities = setOf(
            "Common",
            "Uncommon",
            "Rare"

        )

        relics.forEach { relic ->

            relic.drops.forEach { drop ->

                if (drop.id.isBlank() || drop.itemName.isBlank()) {

                    errors.add("Relic '${relic.id}' contains an empty drop.")

                }

                if (drop.rarity !in validRarities) {
                    errors.add("Invalid rarity '${drop.rarity}' in relic '${relic.id}'.")
                }

            }

        }

    }
}
