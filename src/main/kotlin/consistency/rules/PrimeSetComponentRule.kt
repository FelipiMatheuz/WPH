package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimePart

class PrimeSetComponentRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating prime set components...")

        val ids = context.primeSets
            .map { it.id }
            .toHashSet()

        return buildList {

            context.primeSets.forEach { set ->

                set.components
                    .filter { it.part == PrimePart.PRIME_SET }
                    .forEach {

                        if (it.id !in ids) {

                            add(
                                ValidationError(
                                    FileSource.PRIME_SETS,
                                    "${set.id} references missing PrimeSet '${it.id}'"
                                )
                            )

                        }

                    }

            }

        }

    }

}