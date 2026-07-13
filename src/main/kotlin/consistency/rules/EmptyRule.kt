package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource

class EmptyRule : ConsistencyRule {
    override fun validate(context: ConsistencyContext): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating empty files...")

        return buildList {
            validateEmpty(
                FileSource.PRIME_SETS,
                context.primeSets
            )?.also(::add)

            validateEmpty(
                FileSource.PRIME_COLLECTIONS,
                context.primeCollections
            )?.also(::add)

            validateEmpty(
                FileSource.RELICS,
                context.relics
            )?.also(::add)
        }
    }

    private inline fun <reified T> validateEmpty(
        source: FileSource,
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