package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import model.domain.prime.PrimePart
import model.domain.prime.PrimeSet

class FarmabilityRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        val relicDrops = context.relics
            .flatMap { relic -> relic.drops }
            .map { it.id }
            .toHashSet()

        val primeSets = context.primeSets
            .associateBy { it.id }

        val errors = mutableListOf<ValidationError>()

        context.primeSets.forEach {

            validatePrimeSet(
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

    private fun validatePrimeSet(
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
                source = "PrimeSets",
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
                            source = "PrimeSets",
                            message = "'${primeSet.id}' references missing PrimeSet '${component.id}'."
                        )

                    } else {

                        validatePrimeSet(
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
                            source = "Relics",
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