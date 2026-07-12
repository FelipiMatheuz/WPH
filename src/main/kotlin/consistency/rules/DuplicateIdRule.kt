package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import consistency.model.ValidationSource

class DuplicateIdRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        return buildList {

            validateDuplicates(
                ValidationSource.PRIME_SETS,
                context.primeSets.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                ValidationSource.PRIME_COLLECTIONS,
                context.primeCollections.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                ValidationSource.RELICS,
                context.relics.map { it.id }
            ).also(::addAll)
        }
    }

    private fun validateDuplicates(
        source: ValidationSource,
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