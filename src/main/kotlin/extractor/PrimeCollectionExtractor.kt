package extractor

import logging.Logger
import model.domain.FileSource
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

        Logger.info(FileSource.PRIME_COLLECTIONS.logName, "Extracting prime access info...")
        val currentPrimeAccessHeading = document
            .selectFirst("h1#Current_Prime_Access")
            ?: Logger.error(
                FileSource.PRIME_COLLECTIONS.logName,
                "Current Prime Access section not found."
            )

        val currentPrimeHeading = currentPrimeAccessHeading
            .parent()
            ?.nextElementSibling()
            ?: Logger.error(
                FileSource.PRIME_COLLECTIONS.logName,
                "Current Prime heading not found."
            )

        val content = currentPrimeHeading
            .nextElementSibling()?.child(0)
            ?: Logger.error(
                FileSource.PRIME_COLLECTIONS.logName,
                "Current Prime content not found."
            )

        val releaseText = content.children()
            .firstOrNull { it.tagName() == "p" }
            ?.text()
            ?: Logger.error(FileSource.PRIME_COLLECTIONS.logName, "Release date not found")

        val released = parseReleaseDate(releaseText)

        val table = content
            .selectFirst("table.article-table")
            ?: Logger.error(FileSource.PRIME_COLLECTIONS.logName, "Prime Access table not found.")

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
                ?: Logger.error(
                    FileSource.PRIME_COLLECTIONS.logName,
                    "Current Prime Warframe not found."
                ),
            released = released,
            items = items
        )
    }

    private fun parseReleaseDate(text: String): Int {

        val dateString = Regex("""from\s+(.+?)\.""")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?: Logger.error(
                FileSource.PRIME_COLLECTIONS.logName,
                "Unable to parse release date: $text"
            )

        val date = LocalDate.parse(dateString, DATE_FORMAT)

        return date.format(DateTimeFormatter.BASIC_ISO_DATE).toInt()
    }
}