package exporter

import kotlinx.serialization.json.Json
import model.domain.FileSource
import model.domain.prime.PrimeSet
import model.domain.relic.Relic
import model.raw.Manifest
import model.raw.ManifestFile
import misc.sha256
import java.io.File
import java.time.Instant
import kotlin.collections.sortedBy

class JsonExporter {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private val dataDirectory = File("data")

    fun exportRelics(data: List<Relic>) {

        println("Exporting relics.json...")
        val sortedData = data.sortedWith(
            compareBy(
                Relic::era,
                Relic::name
            )
        )
        writeJson(sortedData, FileSource.RELICS.path)
    }

    fun exportPrimeSets(data: List<PrimeSet>) {

        println("Exporting prime_sets.json...")
        val sortedData = data.sortedBy { it.name }
        writeJson(sortedData, FileSource.PRIME_SETS.path)
    }

    fun exportManifest() {

        println("Exporting manifest.json...")
        val files = dataDirectory
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

        writeJson(manifest, FileSource.MANIFEST.path)
    }

    private inline fun <reified T> writeJson(encoded: T, path: String) {
        val text = json.encodeToString(encoded)

        val file = File(dataDirectory.path +"/"+ path)
        file.parentFile.mkdirs()
        file.writeText(text)
        println("$path exported successfully")
    }
}
