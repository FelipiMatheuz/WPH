package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimePart
import model.domain.prime.PrimeSet

class FarmabilityRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating farm tracking for items...")

        val relicDrops = context.relics
            .flatMap { relic -> relic.drops }
            .map { it.id }
            .toHashSet()

        val primeSets = context.primeSets
            .associateBy { it.id }

        val errors = mutableListOf<ValidationError>()

        context.primeSets.forEach {

            validatePrimeSet(it.id, relicDrops, errors)

            validatePrimeSetComponents(
                primeSet = it,
                primeSets = primeSets,
                relicDrops = relicDrops,
                visiting = mutableSetOf(),
                validated = mutableSetOf(),
                errors = errors
            )

        }

        return errors
    }

    fun validatePrimeSet(
        primeSetId: String,
        relicDrops: Set<String>,
        errors: MutableList<ValidationError>
    ) {
        if (primeSetId !in relicDrops) {
            errors += ValidationError(
                source = FileSource.RELICS,
                message = "PrimeSet '${primeSetId}' blueprint cannot be obtained from any relic."
            )
        }
    }

    private fun validatePrimeSetComponents(
        primeSet: PrimeSet,
        primeSets: Map<String, PrimeSet>,
        relicDrops: Set<String>,
        visiting: MutableSet<String>,
        validated: MutableSet<String>,
        errors: MutableList<ValidationError>
    ) {

        if (primeSet.id in validated)
            return

        if (!visiting.add(primeSet.id)) {

            errors += ValidationError(
                source = FileSource.PRIME_SETS,
                message = "Circular dependency detected involving '${primeSet.id}'."
            )

            return
        }

        primeSet.components.forEach { component ->

            when (component.part) {

                PrimePart.PRIME_SET -> {

                    val dependency = primeSets[component.id]

                    if (dependency == null) {

                        errors += ValidationError(
                            source = FileSource.PRIME_SETS,
                            message = "'${primeSet.id}' references missing PrimeSet '${component.id}'."
                        )

                    } else {

                        validatePrimeSetComponents(
                            dependency,
                            primeSets,
                            relicDrops,
                            visiting,
                            validated,
                            errors
                        )

                    }

                }

                else -> {

                    if (component.id !in relicDrops) {

                        errors += ValidationError(
                            source = FileSource.RELICS,
                            message = "Component '${component.id}' required by '${primeSet.id}' cannot be obtained from any relic."
                        )

                    }

                }

            }

        }

        visiting.remove(primeSet.id)
        validated.add(primeSet.id)
    }
}