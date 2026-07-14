package misc

import logging.LogMetadata
import logging.Logger
import manager.FileManager
import model.domain.FileSource
import model.raw.RawPrimeSetWithComponents
import model.raw.ValidatedPrimeSet

object IgnoredPrimeSets {

    private val file = FileManager.configFile(FileSource.IGNORED)
    fun load(): Set<String> {
        Logger.info("Loading ignored prime sets list...", FileSource.IGNORED.logName)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            Logger.warn(
                "File ${FileSource.IGNORED.path} not found.",
                listOf(LogMetadata("Path", file.path))
            )
            return emptySet()
        }

        return file
            .readLines()
            .map(String::trim)
            .filter(String::isNotBlank)
            .toSet()
    }

    fun update(rawPrimeSets: List<RawPrimeSetWithComponents>): List<ValidatedPrimeSet> {
        val ignoredItems =
            rawPrimeSets.filter { it.components.isNullOrEmpty() }.map { it.rawSet.name }
        val existingIgnored = load()

        val newIgnoredLog = mutableListOf<String>()
        for (ignored in ignoredItems) {
            if (ignored in existingIgnored) {
                continue
            }
            newIgnoredLog.add(ignored)
            file.appendText("\n$ignored")
        }

        if (newIgnoredLog.isNotEmpty()) {
            Logger.info(
                "New set(s) ignored", FileSource.IGNORED.logName,
                newIgnoredLog.map { LogMetadata("", it) })
        }

        return rawPrimeSets.mapNotNull { item ->
            item.components?.let { components ->
                ValidatedPrimeSet(
                    rawSet = item.rawSet,
                    components = components
                )
            }
        }
    }
}