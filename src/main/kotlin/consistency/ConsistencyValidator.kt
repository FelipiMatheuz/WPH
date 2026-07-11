package consistency

import consistency.model.ConsistencyContext
import consistency.rules.CollectionPrimeSetRule
import consistency.rules.ConsistencyRule
import consistency.rules.DuplicateIdRule
import consistency.rules.FarmabilityRule
import consistency.rules.ManifestRule
import consistency.rules.PrimeSetComponentRule

class ConsistencyValidator {

    private val rules: List<ConsistencyRule> = listOf(
        CollectionPrimeSetRule(),
        DuplicateIdRule(),
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
                println("[ERROR] ${it.source}: ${it.message}")
            }

            error("${errors.size} consistency errors found.")

        }

    }

}