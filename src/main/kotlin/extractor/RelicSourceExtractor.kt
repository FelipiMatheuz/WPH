package extractor

import logging.Logger
import model.domain.relic.AcquisitionSource
import model.raw.RawRelicSource
import misc.IdGenerator
import model.domain.FileSource
import org.jsoup.nodes.Document

class RelicSourceExtractor {
    fun extract(voidDocument: Document, resurgenceDocument: Document): List<RawRelicSource> {
        Logger.info(FileSource.RELICS.logName, "Extracting list of relics sources...")
        return buildList {

            addAll(
                extractVoidTable(
                    voidDocument,
                    "Unvaulted/Available Relics",
                    AcquisitionSource.MISSION
                )
            )

            addAll(
                extractVoidTable(
                    voidDocument,
                    "Baro Ki'Teer Exclusive Relics",
                    AcquisitionSource.BARO
                )
            )

            addAll(
                extractResurgenceTable(
                    resurgenceDocument
                )
            )

        }
    }

    private fun extractVoidTable(
        document: Document,
        caption: String,
        source: AcquisitionSource
    ): List<RawRelicSource> {
        val table = document
            .select("table")
            .firstOrNull {
                it.selectFirst("caption")
                    ?.text()
                    ?.startsWith(caption) == true
            } ?: Logger.error(
            FileSource.RELICS.logName,
            "No ${source.name.lowercase()} table found"
        )

        val relicRegex = Regex("^(Lith|Meso|Neo|Axi)\\s+[A-Z][0-9]+$")

        val relicList = table.select("a")
            .map { it.text() }
            .filter { relicRegex.matches(it) }
        Logger.info(FileSource.RELICS.logName, "$caption: ${relicList.size}")
        return relicList.map { RawRelicSource(IdGenerator.generateId(it), source) }
    }

    private fun extractResurgenceTable(document: Document): List<RawRelicSource> {
        val activeRow = document
            .select("tr")
            .firstOrNull {
                it.selectFirst("div.posTextIcon") != null
            } ?: Logger.error(FileSource.RELICS.logName, "No active Resurgence found")

        val table = activeRow.selectFirst("table[data-tableid=CollectedRelics]")
            ?: Logger.error(FileSource.RELICS.logName, "CollectedRelics table not found")

        val relicList = table
            .select("tr[data-rowid]")
            .map { row ->

                val name = row
                    .select("a")
                    .last()!!
                    .text()

                RawRelicSource(
                    relicId = IdGenerator.generateId(name),
                    source = AcquisitionSource.RESURGENCE
                )
            }
        Logger.info(FileSource.RELICS.logName, "Varzia Resurgence Relics: ${relicList.size}")
        return relicList
    }
}