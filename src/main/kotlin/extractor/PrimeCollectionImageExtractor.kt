package extractor

import logging.Logger
import org.jsoup.nodes.Document

class PrimeCollectionImageExtractor {

    fun extract(document: Document): String {
        Logger.info("Getting URL image for new prime collection...")

        val style = document
            .selectFirst("div.SectionBackground--masthead")
            ?.attr("style")

        val prefix = "url("
        val start = style?.indexOf(prefix) ?: 0
        val end = style?.indexOf(')', start) ?: 0

        val urlImage = style?.substring(start + prefix.length, end)?.trim('\'', '"')

        return if (urlImage != null) {
            urlImage
        } else {
            Logger.warn("Prime Access background not found")
            ""
        }
    }
}