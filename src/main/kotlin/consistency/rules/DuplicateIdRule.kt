package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource

class DuplicateIdRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating duplicated ids...")

        return buildList {

            validateDuplicates(
                FileSource.PRIME_SETS,
                context.primeSets.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                FileSource.PRIME_COLLECTIONS,
                context.primeCollections.map { it.id }
            ).also(::addAll)

            validateDuplicates(
                FileSource.RELICS,
                context.relics.map { it.id }
            ).also(::addAll)
        }
    }

    private fun validateDuplicates(
        source: FileSource,
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