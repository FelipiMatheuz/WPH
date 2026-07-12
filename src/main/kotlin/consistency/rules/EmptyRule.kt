package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError

class EmptyRule : ConsistencyRule {
    override fun validate(context: ConsistencyContext): List<ValidationError> {

        return buildList {
            validateEmpty(
                "PrimeSet",
                context.primeSets
            )?.also(::add)

            validateEmpty(
                "PrimeCollections",
                context.primeCollections
            )?.also(::add)

            validateEmpty(
                "Relics",
                context.relics
            )?.also(::add)
        }
    }

    private inline fun <reified T> validateEmpty(
        source: String,
        list: List<T>
    ): ValidationError? {
        return if (list.isEmpty()) {
            ValidationError(
                source,
                "$source file is empty."
            )
        } else {
            null
        }
    }
}