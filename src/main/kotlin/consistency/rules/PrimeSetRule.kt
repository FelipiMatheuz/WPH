package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimePart
import model.domain.prime.PrimeSet
import kotlin.collections.forEach

class PrimeSetRule : ConsistencyRule {

    override fun validate(context: ConsistencyContext): List<LogMetadata> {

        Logger.info("Validating prime sets...")

        return buildList {
            val primeSets = context.primeSets
            validateNames(primeSets).also(::addAll)
            validateComponents(primeSets).also(::addAll)
            validateReferences(primeSets).also(::addAll)
        }
    }

    private fun validateNames(primeSets: List<PrimeSet>): List<LogMetadata> {

        val listErrors: MutableList<LogMetadata> = mutableListOf()
        primeSets.forEach {
            if (it.name.isBlank()) {
                listErrors.add(
                    LogMetadata(
                        FileSource.PRIME_SETS.logName,
                        "PrimeSet '${it.id}' has empty name."
                    )
                )
            }
            if (it.components.isEmpty()) {
                listErrors.add(
                    LogMetadata(
                        FileSource.PRIME_SETS.logName,
                        "PrimeSet '${it.id}' has no components."
                    )
                )
            }
        }
        return listErrors
    }

    private fun validateComponents(primeSets: List<PrimeSet>): List<LogMetadata> {

        val listErrors: MutableList<LogMetadata> = mutableListOf()
        primeSets.forEach { set ->
            set.components.forEach { component ->

                if (component.quantity <= 0) {
                    listErrors.add(
                        LogMetadata(
                            FileSource.PRIME_SETS.logName,
                            "Component '${component.id}' of '${set.name}' has invalid quantity."
                        )
                    )
                }

                if (component.id.isBlank()) {
                    listErrors.add(
                        LogMetadata(
                            FileSource.PRIME_SETS.logName,
                            "PrimeSet '${set.name}' contains empty component id."
                        )
                    )
                }
            }
        }
        return listErrors
    }

    private fun validateReferences(primeSets: List<PrimeSet>): List<LogMetadata> {

        val ids = primeSets.map { it.id }.toSet()
        val listErrors: MutableList<LogMetadata> = mutableListOf()

        primeSets.forEach { set ->
            set.components
                .filter { it.part == PrimePart.PRIME_SET }
                .forEach {
                    if (it.id !in ids) {
                        listErrors.add(
                            LogMetadata(
                                FileSource.PRIME_SETS.logName,
                                "PrimeSet '${set.name}' references unknown PrimeSet '${it.id}'."
                            )
                        )
                    }
                }
        }
        return listErrors
    }
}