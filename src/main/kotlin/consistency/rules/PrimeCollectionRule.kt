package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.collections.forEach

class PrimeCollectionRule : ConsistencyRule {

    override fun validate(context: ConsistencyContext): List<LogMetadata> {

        Logger.info("Validating prime collections...")

        return buildList {
            val collections = context.primeCollections
            validateReleaseDates(collections).also(::addAll)
            validateCollection(collections).also(::addAll)
            validateOrdering(collections)?.also(::add)
        }
    }

    private fun validateReleaseDates(
        collections: List<PrimeCollection>
    ): List<LogMetadata> {
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val listErrors: MutableList<LogMetadata> = mutableListOf()

        collections.forEach { collection ->
            try {
                LocalDate.parse(
                    collection.released.toString(),
                    formatter
                )
            } catch (_: DateTimeParseException) {
                listErrors.add(
                    LogMetadata(
                        FileSource.PRIME_COLLECTIONS.logName,
                        "Invalid release date '${collection.released}' for collection '${collection.id}'."
                    )
                )
            }
        }
        return listErrors
    }

    private fun validateCollection(
        collections: List<PrimeCollection>
    ): List<LogMetadata> {

        val listErrors: MutableList<LogMetadata> = mutableListOf()
        collections.forEach { collection ->
            if (collection.primeSets.isEmpty()) {
                listErrors.add(
                    LogMetadata(
                        FileSource.PRIME_COLLECTIONS.logName,
                        "${collection.id} has no Prime Sets."
                    )
                )
            } else if (collection.primeSets.count { it.contains(collection.name, true) } == 0) {
                listErrors.add(
                    LogMetadata(
                        FileSource.PRIME_COLLECTIONS.logName,
                        "${collection.id} must contain the owner Warframe in collection."
                    )
                )
            }
        }
        return listErrors
    }

    private fun validateOrdering(
        collections: List<PrimeCollection>
    ): LogMetadata? {
        val ordered = collections
            .sortedByDescending { it.released }

        return if (ordered != collections) {
            LogMetadata(
                FileSource.PRIME_COLLECTIONS.logName,
                "Prime Collections must be ordered by release date (newest first)."
            )
        } else {
            null
        }
    }
}