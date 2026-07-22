package extractor

import logging.LogMetadata
import logging.Logger
import model.domain.relic.Rarity
import model.raw.RawDrop
import model.raw.RawRelic
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class RelicExtractor {

    fun extract(dropTableDocument: Document): List<RawRelic> {

        Logger.info("Extracting relics info...")
        val relics = mutableListOf<RawRelic>()

        var pendingHeader: Pair<String, String>? = null
        val dropsBuffer = mutableListOf<RawDrop>()

        relicTable(dropTableDocument)?.select("tr")?.forEach { row ->
            when {
                isRelicHeader(row) -> {
                    pendingHeader?.let { (era, name) ->
                        relics.add(RawRelic(name, era, dropsBuffer.toList()))
                    }

                    dropsBuffer.clear()
                    val header = row.selectFirst("th")!!.text()
                    pendingHeader =
                        if (header.endsWith("(Intact)") && !header.contains("Requiem")) {
                            val raw = parseHeader(row)
                            raw.era to raw.name
                        } else null
                }

                isDropRow(row) -> {
                    pendingHeader?.let {
                        dropsBuffer.add(parseDrop(row))
                    }
                }
            }
        }
        pendingHeader?.let { (era, name) ->
            relics.add(RawRelic(name, era, dropsBuffer.toList()))
        }

        Logger.info(
            "Extraction completed", null, listOf(
                LogMetadata("count", relics.size.toString())
            )
        )
        return relics
    }

    private fun relicTable(document: Document): Element? = document
        .select("h3")
        .first { it.text() == "Relics:" }
        .nextElementSibling()

    private fun isRelicHeader(row: Element): Boolean {
        return row.selectFirst("th")
            ?.text()
            ?.contains("Relic") == true
    }

    private fun isDropRow(row: Element): Boolean {

        return row.select("td").size == 2

    }

    private fun parseHeader(row: Element): RawRelic {

        val header = row
            .selectFirst("th")!!
            .text()


        val relic = header
            .substringBefore(" Relic")
            .trim()


        val parts = relic.split(" ")

        return RawRelic(
            name = parts[1],
            era = parts[0],
            drops = listOf()
        )
    }

    private fun parseDrop(row: Element): RawDrop {

        val cells = row.select("td")

        val fullName = cells[0].text()

        val namedRarity = cells[1]
            .text()
            .substringBefore(" (")

        val percentageRarity = cells[1]
            .text()
            .substringAfter("(").replace(")","")

        val rarity = Rarity.fromTable(namedRarity, percentageRarity)

        val itemName = fullName
            .replace("Blueprint", "")
            .replace("Kubrow Collar", "")
            .trim()

        if (rarity == null) {
            Logger.error("Rarity $rarity unsupported for $itemName")
        }

        return RawDrop(
            itemName = itemName,
            rarity = rarity
        )
    }
}
