package misc

import manager.FileManager
import model.domain.FileSource
import model.raw.RawPrimeSetWithComponents

object IgnoredPrimeSets {

    private val file = FileManager.configFile(FileSource.IGNORED)
    fun load(): Set<String> {

        if (!file.exists()) {
            file.parentFile.mkdirs()
            return emptySet()
        }

        return file
            .readLines()
            .map(String::trim)
            .filter(String::isNotBlank)
            .toSet()
    }

    fun update(rawPrimeSets: List<RawPrimeSetWithComponents>): List<RawPrimeSetWithComponents> {
        val ignoredItems = rawPrimeSets.filter { it.components.isNullOrEmpty() }.map { it.rawSet.name }
        val existingIgnored = load()

        println("New ignored items:")
        for (ignored in ignoredItems) {
            if(ignored in existingIgnored)
                continue
            println(ignored)
            file.appendText("\n$ignored")
        }

        return rawPrimeSets.filter { !it.components.isNullOrEmpty() }
    }
}