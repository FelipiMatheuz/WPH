package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError

class CollectionPrimeSetRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        val ids = context.primeSets
            .map { it.id }
            .toHashSet()

        return buildList {

            context.primeCollections.forEach { collection ->

                collection.primeSets.forEach { setId ->

                    if (setId !in ids) {

                        add(
                            ValidationError(
                                "PrimeCollection",
                                "${collection.id} references unknown PrimeSet '$setId'"
                            )
                        )

                    }

                }

            }

        }

    }

}