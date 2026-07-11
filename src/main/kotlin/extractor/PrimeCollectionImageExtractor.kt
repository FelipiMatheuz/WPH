package extractor

import org.jsoup.nodes.Document

class PrimeCollectionImageExtractor {

    fun extract(document: Document): String {
        val style = document
            .selectFirst("div.SectionBackground--masthead")
            ?.attr("style")
            ?: error("Prime Access masthead not found")

        val prefix = "url("
        val start = style.indexOf(prefix)
        require(start != -1) { "Poster URL not found" }

        val end = style.indexOf(')', start)
        require(end != -1) { "Poster URL not found" }

        return style
            .substring(start + prefix.length, end)
            .trim('\'', '"')
    }
}