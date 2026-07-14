package consistency.rules

import consistency.model.ConsistencyContext
import logging.LogMetadata
import logging.Logger
import model.domain.FileSource

class ManifestRule : ConsistencyRule {

    override fun validate(
        context: ConsistencyContext
    ): List<LogMetadata> {

        Logger.info("Validating manifest file logs...")

        val expected = FileSource.entries.map { it.path }
            .filter { it != FileSource.MANIFEST.path && it.contains(".json") }
            .toSet()

        val files = context.manifest.files
            .map { file -> file.name }
            .toSet()

        return expected
            .minus(files)
            .map {

                LogMetadata(
                    FileSource.MANIFEST.logName,
                    "$it missing from manifest."
                )

            }

    }

}