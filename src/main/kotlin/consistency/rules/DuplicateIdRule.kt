package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource

class DuplicateIdRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<LogMetadata> {

        Logger.info("Validating duplicated ids...")

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
    ): List<LogMetadata> {

        return ids.groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .keys
            .map {
                LogMetadata(
                    source.logName,
                    "Duplicated id '$it'"
                )
            }

    }

}