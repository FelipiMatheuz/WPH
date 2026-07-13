package misc

import logging.Logger
import manager.FileManager
import model.domain.FileSource
import model.raw.RawPrimeSetWithComponents
import model.raw.ValidatedPrimeSet

object IgnoredPrimeSets {

    private val file = FileManager.configFile(FileSource.IGNORED)
    fun load(): Set<String> {
        Logger.info(FileSource.IGNORED.logName, "Loading ignored prime sets list...")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            Logger.warn(FileSource.IGNORED.logName, "No JSON file found.")
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
            Logger.info(FileSource.IGNORED.logName, "New set(s) ignored:\n$newIgnoredLog")
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