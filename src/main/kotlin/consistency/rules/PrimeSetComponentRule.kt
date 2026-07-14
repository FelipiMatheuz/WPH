package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimePart

class PrimeSetComponentRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<LogMetadata> {

        Logger.info("Validating prime set components...")

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
                                LogMetadata(
                                    FileSource.PRIME_SETS.logName,
                                    "${set.id} references missing PrimeSet '${it.id}'"
                                )
                            )

                        }

                    }

            }

        }

    }

}