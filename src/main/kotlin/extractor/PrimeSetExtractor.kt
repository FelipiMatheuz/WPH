package extractor

import model.domain.prime.PrimeType
import model.raw.RawPrimeSet
import org.jsoup.nodes.Document

class PrimeSetExtractor {
    fun extract(document: Document): List<RawPrimeSet> {

        val result = mutableListOf<RawPrimeSet>()

        println("========== Prime Catalog ==========")

        document.select("div.mw-heading3").forEach { heading ->

            val title = heading
                .selectFirst("h3")
                ?.text()
                ?.trim()
                ?: return@forEach

            val type = PrimeType.fromWiki(title)

            if (type == null) {
                println("Skipping unsupported category: $title")
                return@forEach
            }

            println()
            println("Category: $type")

            val gallery = heading.nextElementSibling()

            if (gallery == null || !gallery.hasClass("gallery")) {
                println("Gallery not found for $title")
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

                    val name = link.attr("title").replace("/", " ").trim()

                    val pagePath = link.attr("href").let {
                        if (it.startsWith("http"))
                            it
                        else
                            "https://wiki.warframe.com$it"
                    }

                    val imageUrl = item
                        .selectFirst(".thumb img")
                        ?.attr("src")
                        ?.let {
                            if (it.startsWith("http"))
                                it
                            else
                                "https://wiki.warframe.com$it"
                        }
                        ?: ""

                    println(" -> $name")

                    result += RawPrimeSet(
                        name = name,
                        type = type,
                        imageUrl = imageUrl,
                        pageUrl = pagePath,
                        components = null
                    )
                }
        }

        println()
        println("Prime Sets extracted: ${result.size}")

        return result
    }
}