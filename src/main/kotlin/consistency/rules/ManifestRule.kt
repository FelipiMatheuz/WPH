package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError

class ManifestRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        val expected = setOf(
            "relics.json",
            "prime_sets.json",
            "prime_collection.json"
        )

        val files = context.manifest.files
            .map { file -> file.name }
            .toSet()

        return expected
            .minus(files)
            .map {

                ValidationError(
                    "Manifest",
                    "$it missing from manifest."
                )

            }

    }

}