package consistency

import consistency.model.ConsistencyContext
import consistency.rules.CollectionPrimeSetRule
import consistency.rules.ConsistencyRule
import consistency.rules.DuplicateIdRule
import consistency.rules.EmptyRule
import consistency.rules.FarmabilityRule
import consistency.rules.ManifestRule
import consistency.rules.PrimeCollectionRule
import consistency.rules.PrimeSetComponentRule
import consistency.rules.PrimeSetRule
import consistency.rules.RelicRule
import logging.Logger

class ConsistencyValidator {

    private val rules: List<ConsistencyRule> = listOf(
        EmptyRule(),
        DuplicateIdRule(),
        RelicRule(),
        PrimeSetRule(),
        PrimeCollectionRule(),
        CollectionPrimeSetRule(),
        PrimeSetComponentRule(),
        FarmabilityRule(),
        ManifestRule()
    )

    fun validate(
        context: ConsistencyContext
    ) {

        val errors = rules.flatMap {
            it.validate(context)
        }

        if (errors.isNotEmpty()) {

            val metadata = mutableMapOf<String, String>()
            errors.forEach {
                metadata[it.source.logName] = it.message
            }

            Logger.error("CONSISTENCY", "${errors.size} consistency error(s) found.", metadata)
        }
    }

}