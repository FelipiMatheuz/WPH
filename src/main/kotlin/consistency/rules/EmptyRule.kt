package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import consistency.model.ValidationSource

class EmptyRule : ConsistencyRule {
    override fun validate(context: ConsistencyContext): List<ValidationError> {

        return buildList {
            validateEmpty(
                ValidationSource.PRIME_SETS,
                context.primeSets
            )?.also(::add)

            validateEmpty(
                ValidationSource.PRIME_COLLECTIONS,
                context.primeCollections
            )?.also(::add)

            validateEmpty(
                ValidationSource.RELICS,
                context.relics
            )?.also(::add)
        }
    }

    private inline fun <reified T> validateEmpty(
        source: ValidationSource,
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