package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError

class DuplicateIdRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        return buildList {

            validateDuplicates(
                "PrimeSet",
                context.primeSets.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                "PrimeCollections",
                context.primeCollections.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                "Relics",
                context.relics.map { it.id }
            ).also(::addAll)
        }
    }

    private fun validateDuplicates(
        source: String,
        ids: List<String>
    ): List<ValidationError> {

        return ids.groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .keys
            .map {
                ValidationError(
                    source,
                    "Duplicated id '$it'"
                )
            }

    }

}