package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import model.domain.prime.PrimePart
import model.domain.prime.PrimeSet
import kotlin.collections.forEach

class PrimeSetRule : ConsistencyRule {

    override fun validate(context: ConsistencyContext): List<ValidationError> {
        return buildList {
            val primeSets = context.primeSets
            validateNames(primeSets).also(::addAll)
            validateComponents(primeSets).also(::addAll)
            validateReferences(primeSets).also(::addAll)
        }
    }

    private fun validateNames(primeSets: List<PrimeSet>): List<ValidationError> {

        val listErrors: MutableList<ValidationError> = mutableListOf()
        primeSets.forEach {
            if (it.name.isBlank()) {
                listErrors.add(
                    ValidationError(
                        "PrimeSet",
                        "PrimeSet '${it.id}' has empty name."
                    )
                )
            }
            if (it.components.isEmpty()) {
                listErrors.add(
                    ValidationError(
                        "PrimeSet",
                        "PrimeSet '${it.id}' has no components."
                    )
                )
            }
        }
        return listErrors
    }

    private fun validateComponents(primeSets: List<PrimeSet>): List<ValidationError> {

        val listErrors: MutableList<ValidationError> = mutableListOf()
        primeSets.forEach { set ->
            set.components.forEach { component ->

                if (component.quantity <= 0) {
                    listErrors.add(
                        ValidationError(
                            "PrimeSet",
                            "Component '${component.id}' of '${set.name}' has invalid quantity."
                        )
                    )
                }

                if (component.id.isBlank()) {
                    listErrors.add(
                        ValidationError(
                            "PrimeSet",
                            "PrimeSet '${set.name}' contains empty component id."
                        )
                    )
                }
            }
        }
        return listErrors
    }

    private fun validateReferences(primeSets: List<PrimeSet>): List<ValidationError> {

        val ids = primeSets.map { it.id }.toSet()
        val listErrors: MutableList<ValidationError> = mutableListOf()

        primeSets.forEach { set ->
            set.components
                .filter { it.part == PrimePart.PRIME_SET }
                .forEach {
                    if (it.id !in ids) {
                        listErrors.add(
                            ValidationError(
                                "PrimeSet",
                                "PrimeSet '${set.name}' references unknown PrimeSet '${it.id}'."
                            )
                        )
                    }
                }
        }
        return listErrors
    }
}