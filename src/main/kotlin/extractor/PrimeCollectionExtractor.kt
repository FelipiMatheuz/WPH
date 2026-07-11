package extractor

import model.raw.RawPrimeCollection
import org.jsoup.nodes.Document
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PrimeCollectionExtractor {

    companion object {
        private val DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    }
    fun extract(document: Document): RawPrimeCollection {

        val currentPrimeAccessHeading = document
            .selectFirst("h1#Current_Prime_Access")
            ?: error("Current Prime Access section not found.")

        val currentPrimeHeading = currentPrimeAccessHeading
            .parent()
            ?.nextElementSibling()
            ?: error("Current Prime heading not found.")

        val content = currentPrimeHeading
            .nextElementSibling()?.child(0)
            ?: error("Current Prime content not found.")

        val releaseText = content.children()
            .firstOrNull { it.tagName() == "p" }
            ?.text()
            ?: error("Release date not found")

        val released = parseReleaseDate(releaseText)

        val table = content
            .selectFirst("table.article-table")
            ?: error("Prime Access table not found.")

        val items = mutableListOf<String>()

        var warframeName: String? = null

        table.select("tr").drop(1).forEach { row ->

            val header = row.selectFirst("th") ?: return@forEach

            val text = header.text().trim()

            when {
                text.contains("Glyph", true) -> return@forEach
                text.contains("Accessories", true) -> return@forEach
                text.startsWith("Price", true) -> return@forEach
                !text.endsWith("Prime") -> return@forEach
            }

            items += text

            val link = header.selectFirst("a[href]")

            if (link != null && "/Prime" in link.attr("href")) {
                warframeName = text.removeSuffix(" Prime")
            }
        }

        return RawPrimeCollection(
            name = warframeName
                ?: error("Current Prime Warframe not found."),
            released = released,
            promoUrl = "",
            items = items
        )
    }

    private fun parseReleaseDate(text: String): Int {

        val dateString = Regex("""from\s+(.+?)\.""")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?: error("Unable to parse release date: $text")

        val date = LocalDate.parse(dateString, DATE_FORMAT)

        return date.format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
    }
}