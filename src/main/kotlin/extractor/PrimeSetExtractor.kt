package extractor

import logging.LogMetadata
import logging.Logger
import model.domain.prime.PrimeType
import model.raw.RawPrimeSet
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import remote.DataSources

class PrimeSetExtractor {

    fun extract(primeDocument: Document): List<RawPrimeSet> {

        Logger.info("Extracting prime set catalog...")

        val result = mutableListOf<RawPrimeSet>()
        var skippedCount = 0

        Logger.info("===PRIME CATALOG===")

        primeDocument.select("div.mw-heading3").forEach { heading ->

            val title = heading
                .selectFirst("h3")
                ?.text()
                ?.trim()
                ?: return@forEach

            val type = PrimeType.fromWiki(title)

            if (type == null) {
                Logger.info("Skipping unsupported category: $title")
                return@forEach
            }

            Logger.info("Category: $type")

            val gallery = heading.nextElementSibling()

            if (gallery == null || !gallery.hasClass("gallery")) {
                Logger.warn("Gallery not found for $title")
                return@forEach
            }

            gallery
                .select("li.gallerybox")
                .forEach { item ->

                    val galleryText = item.selectFirst(".gallerytext")
                        ?: return@forEach

                    if (shouldSkipPrimeSet(galleryText)) {

                        val skippedName =
                            galleryText.selectFirst("a")?.attr("title")?.replace("/", " ")
                                ?.replace("Collar", "")?.trim() ?: "Unknown"

                        Logger.info("    └──> Skipping non-relic Prime: $skippedName")
                        skippedCount++

                        return@forEach
                    }

                    val link = galleryText
                        .select("a")
                        .first()
                        ?: return@forEach

                    val name = link.attr("title").replace("/", " ").replace("Collar", "").trim()

                    val pageUrl = link.attr("href").let {
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

                    Logger.info("    └──> $name")

                    result += RawPrimeSet(
                        name = name,
                        type = type,
                        imageUrl = imageUrl,
                        pageUrl = pageUrl
                    )
                }
        }

        Logger.info(
            "Prime sets extracted", null,
            listOf(
                LogMetadata("Count", result.size.toString()),
                LogMetadata("Skipped", skippedCount.toString())
            )
        )

        return result
    }

    private fun shouldSkipPrimeSet(
        galleryText: Element
    ): Boolean {

        return galleryText
            .select(".hover-over")
            .any { it.attr("title") in NON_RELIC_MARKERS }

    }

    private companion object {

        val NON_RELIC_MARKERS = setOf(
            "Founder-exclusive Prime",
            "Primes with a special source of acquisition"
        )
    }
}