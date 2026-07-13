package consistency.rules

import consistency.model.ConsistencyContext
import consistency.model.ValidationError
import logging.Logger
import model.domain.FileSource

class ManifestRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<ValidationError> {

        Logger.info("CONSISTENCY", "Validating manifest file logs...")

        val expected = FileSource.entries.map { it.path }
            .filter { it != FileSource.MANIFEST.path && it.contains(".json") }
            .toSet()

        val files = context.manifest.files
            .map { file -> file.name }
            .toSet()

        return expected
            .minus(files)
            .map {

                ValidationError(
                    FileSource.MANIFEST,
                    "$it missing from manifest."
                )

            }

    }

}