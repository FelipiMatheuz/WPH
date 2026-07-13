package extractor

import logging.Logger
import model.domain.FileSource
import model.domain.prime.PrimeType
import model.raw.RawPrimeSet
import org.jsoup.nodes.Document
import remote.DataSources

class PrimeSetExtractor {

    fun extract(document: Document): List<RawPrimeSet> {

        Logger.info(FileSource.PRIME_SETS.logName, "Extracting prime set catalog...")

        val result = mutableListOf<RawPrimeSet>()

        Logger.info(FileSource.PRIME_SETS.logName, "    PRIME CATALOG   ")

        document.select("div.mw-heading3").forEach { heading ->

            val title = heading
                .selectFirst("h3")
                ?.text()
                ?.trim()
                ?: return@forEach

            val type = PrimeType.fromWiki(title)

            if (type == null) {
                Logger.info(FileSource.PRIME_SETS.logName, "Skipping unsupported category: $title")
                return@forEach
            }
            Logger.info(FileSource.PRIME_SETS.logName, "\nCategory: $type")

            val gallery = heading.nextElementSibling()

            if (gallery == null || !gallery.hasClass("gallery")) {
                Logger.warn(FileSource.PRIME_SETS.logName, "Gallery not found for $title")
                return@forEach
            }

            gallery
                .select("li.gallerybox")
                .forEach { item ->

                    val galleryText = item.selectFirst(".gallerytext")
                        ?: return@forEach

                    val link = galleryText
                        .select("a")
                        .first()
                        ?: return@forEach

                    val name = link.attr("title").replace("/", " ").replace("Collar", "").trim()

                    val pagePath = link.attr("href").let {
                        if (it.startsWith("http"))
                            it
                        else
                            "${DataSources.WARFRAME_WIKI}$it"
                    }

                    val imageUrl = item
                        .selectFirst(".thumb img")
                        ?.attr("src")
                        ?.let {
                            if (it.startsWith("http"))
                                it
                            else
                                "${DataSources.WARFRAME_WIKI}$it"
                        }
                        ?: ""

                    Logger.info(FileSource.PRIME_SETS.logName, "    └──> $name")

                    result += RawPrimeSet(
                        name = name,
                        type = type,
                        imageUrl = imageUrl,
                        pageUrl = pagePath
                    )
                }
        }

        Logger.info(FileSource.PRIME_SETS.logName, "Prime Sets extracted: ${result.size}")
        return result
    }
}