package extractor

import model.domain.AcquisitionSource
import model.raw.RelicSource
import normalizer.IdGenerator
import org.jsoup.nodes.Document

class RelicSourceExtractor {
    fun extract(voidDocument: Document, resurgenceDocument: Document): List<RelicSource> {
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
    ): List<RelicSource> {
        val table = document
            .select("table")
            .first {
                it.selectFirst("caption")
                    ?.text()
                    ?.startsWith(caption) == true
            } ?: error("No ${source.name.lowercase()} table found")

        val relicRegex = Regex("^(Lith|Meso|Neo|Axi)\\s+[A-Z][0-9]+$")

        val relicList = table.select("a")
            .map { it.text() }
            .filter { relicRegex.matches(it) }
        return relicList.map { RelicSource(IdGenerator.generateId(it), source) }
    }

    private fun extractResurgenceTable(document: Document): List<RelicSource> {
        val activeRow = document
            .select("tr")
            .firstOrNull {
                it.selectFirst("div.posTextIcon") != null
            } ?: error("No active Resurgence found")

        val table = activeRow.selectFirst("table[data-tableid=CollectedRelics]")
            ?: error("CollectedRelics table not found")

        return table
            .select("tr[data-rowid]")
            .map { row ->

                val name = row
                    .select("a")
                    .last()!!
                    .text()

                RelicSource(
                    relicId = IdGenerator.generateId(name),
                    source = AcquisitionSource.RESURGENCE
                )
            }
    }
}