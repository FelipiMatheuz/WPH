package exporter

import kotlinx.serialization.json.Json
import model.domain.Relic
import java.io.File

class RelicsExporter {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    fun export(data: List<Relic>) {

        println("Exporting relics.json...")
        val sortedData = data.sortedWith(
            compareBy(
                Relic::era,
                Relic::name
            )
        )
        val relicListStr = json.encodeToString(sortedData)

        val file = File("data/relics.json")
        file.parentFile.mkdirs()
        file.writeText(relicListStr)
        println("Done!")
    }
}
