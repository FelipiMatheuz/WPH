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

            errors.forEach {
                println("[ERROR] ${it.source.logName}: ${it.message}")
            }
            error("${errors.size} consistency error(s) found.")
        }

    }

}