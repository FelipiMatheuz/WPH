package validator

import model.domain.prime.PrimeCollection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class PrimeCollectionValidator {

    fun validate(collections: List<PrimeCollection>) {
        require(collections.isNotEmpty()) {
            "Prime Collection list is empty."
        }

        validateUniqueIds(collections)
        validateReleaseDates(collections)
        validateCollection(collections)
        validateOrdering(collections)
    }

    private fun validateUniqueIds(
        collections: List<PrimeCollection>
    ) {
        val duplicated = collections
            .groupBy { it.id }
            .filterValues { it.size > 1 }

        require(duplicated.isEmpty()) {
            "Duplicated Prime Collection ids: ${duplicated.keys}"
        }
    }

    private fun validateReleaseDates(
        collections: List<PrimeCollection>
    ) {
        val formatter = DateTimeFormatter.BASIC_ISO_DATE

        collections.forEach { collection ->
            try {
                LocalDate.parse(
                    collection.released.toString(),
                    formatter
                )
            } catch (_: DateTimeParseException) {
                error(
                    "Invalid release date '${collection.released}' " +
                            "for collection '${collection.id}'."
                )
            }
        }
    }

    private fun validateCollection(
        collections: List<PrimeCollection>
    ) {
        collections.forEach { collection ->
            require(collection.primeSets.isNotEmpty()) {
                "${collection.id} has no Prime Sets."
            }
            require(collection.primeSets.count { it.contains(collection.name, true) } == 1) {
                "${collection.id} must contain the owner Warframe in collection."
            }
        }
    }

    private fun validateOrdering(
        collections: List<PrimeCollection>
    ) {
        val ordered = collections
            .sortedByDescending { it.released }

        require(ordered == collections) {
            "Prime Collections must be ordered by release date (newest first)."
        }
    }
}