package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import model.domain.prime.PrimePart

class PrimeSetComponentRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

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
                                    "PrimeSet",
                                    "${set.id} references missing PrimeSet '${it.id}'"
                                )
                            )

                        }

                    }

            }

        }

    }

}