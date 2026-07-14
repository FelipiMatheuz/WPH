package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource

class CollectionPrimeSetRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<LogMetadata> {

        Logger.info("Validating collection prime set ids...")
        val ids = context.primeSets
            .map { it.id }
            .toHashSet()

        return buildList {

            context.primeCollections.forEach { collection ->

                collection.primeSets.forEach { setId ->

                    if (setId !in ids) {

                        add(
                            LogMetadata(
                                FileSource.PRIME_COLLECTIONS.logName,
                                "${collection.id} references unknown PrimeSet '$setId'"
                            )
                        )

                    }

                }

            }

        }

    }

}