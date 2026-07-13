package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource

class CollectionPrimeSetRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating collection prime set ids...")
        val ids = context.primeSets
            .map { it.id }
            .toHashSet()

        return buildList {

            context.primeCollections.forEach { collection ->

                collection.primeSets.forEach { setId ->

                    if (setId !in ids) {

                        add(
                            ValidationError(
                                FileSource.PRIME_COLLECTIONS,
                                "${collection.id} references unknown PrimeSet '$setId'"
                            )
                        )

                    }

                }

            }

        }

    }

}