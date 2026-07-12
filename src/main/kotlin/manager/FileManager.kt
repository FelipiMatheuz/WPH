package manager

import kotlinx.serialization.json.Json
import misc.sha256
import model.domain.FileSource
import model.domain.prime.PrimeCollection
import model.domain.prime.PrimeSet
import model.domain.relic.Relic
import model.raw.Manifest
import model.raw.ManifestFile
import java.io.File
import java.time.Instant

object FileManager {
    private val json = Json { prettyPrint = true; encodeDefaults = true; ignoreUnknownKeys = true }
    private val dataDir = File("data")

    private val configDir = File("config")
    fun dataFile(source: FileSource) = File(dataDir.path + "/" + source.path)
    fun configFile(source: FileSource) = File(configDir.path + "/" + source.path)

    inline fun <reified T> load(source: FileSource): T {
        val file = dataFile(source)
        if (!file.exists()) error("${file.absolutePath} file is missing")
        return Json.decodeFromString(file.readText())
    }

    private inline fun <reified T> save(source: FileSource, data: T) {

        println("Exporting ${source.path}...")
        val text = json.encodeToString(data)

        val file = dataFile(source)
        file.parentFile.mkdirs()
        file.writeText(text)
        println("${source.path} exported successfully")
    }

    fun exportRelics(data: List<Relic>) {

        val sortedData = data.sortedWith(
            compareBy(
                Relic::era,
                Relic::name
            )
        )
        save(FileSource.RELICS, sortedData)
    }

    fun exportPrimeSets(data: List<PrimeSet>) {

        val sortedData = data.sortedBy { it.name }
        save(FileSource.PRIME_SETS, sortedData)
    }

    fun exportPrimeCollections(data: List<PrimeCollection>) {
        save(FileSource.PRIME_COLLECTIONS, data)
    }

    fun exportManifest() {

        val files = dataDir
            .listFiles()
            ?.filter { it.extension == "json" && it.name != FileSource.MANIFEST.path }
            ?.sortedBy { it.name }
            ?.map { file ->
                ManifestFile(
                    name = file.name,
                    size = file.length(),
                    sha256 = sha256(file)
                )
            }
            ?: emptyList()

        val manifest = Manifest(
            generatedAt = Instant.now().toString(),
            generatorVersion = "1.0.0",
            files = files
        )

        save(FileSource.MANIFEST, manifest)
    }
}