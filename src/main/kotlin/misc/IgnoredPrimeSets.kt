package misc

import model.raw.RawPrimeSet
import java.io.File
import kotlin.collections.filter

object IgnoredPrimeSets {

    private val file = File("config/ignored-prime-sets.txt")
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

    fun update(rawPrimeSets: List<RawPrimeSet>): List<RawPrimeSet> {
        val ignoredItems = rawPrimeSets.filter { it.components.isNullOrEmpty() }.map { it.name }
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