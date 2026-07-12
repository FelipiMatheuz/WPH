package extractor

import model.raw.RawDrop
import model.raw.RawRelic
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class RelicExtractor {

    fun extract(document: Document): List<RawRelic> {

        println("Extracting Info...")
        val relics = mutableListOf<RawRelic>()

        var currentRelic: RawRelic? = null

        relicTable(document)
            ?.select("tr")
            ?.forEach { row ->

                when {

                    isRelicHeader(row) -> {

                        val header = row.selectFirst("th")!!.text()
                        currentRelic?.let {
                            relics.add(it)
                        }

                        currentRelic =
                            if (header.endsWith("(Intact)") && !header.contains("Requiem")) {
                                parseHeader(row)
                            } else {
                                null
                            }

                    }

                    isDropRow(row) -> {

                        currentRelic?.drops?.add(parseDrop(row))

                    }

                }

            }

        currentRelic?.let {
            relics.add(it)
        }

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
            era = parts[0]
        )
    }

    private fun parseDrop(row: Element): RawDrop {

        val cells = row.select("td")

        val fullName = cells[0].text()

        val rarity = cells[1]
            .text()
            .substringBefore(" (")

        val itemName = fullName
            .replace("Blueprint", "")
            .replace("Kubrow Collar", "")
            .trim()

        return RawDrop(
            itemName = itemName,
            rarity = rarity
        )
    }
}
