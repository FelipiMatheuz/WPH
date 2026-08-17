package extractor

import logging.Logger
import model.domain.prime.PrimeComponent
import model.domain.prime.PrimePart
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PrimeSetDetailsExtractor(private val primeName: String) {
    fun extract(primeDetailsDocument: Document): List<PrimeComponent>? {
        Logger.info("Getting more details for $primeName...")
        val table = primeDetailsDocument.selectFirst("table.foundrytable")
            ?: return null

        val firstRequirementRow = table.select("tr")[1]

        return firstRequirementRow
            .select("td")
            .mapNotNull(::parseComponent)
            .groupBy { it.part to it.id }
            .map { (_, components) ->
                components.first().copy(quantity = components.sumOf { it.quantity })
            }
    }

    private fun parseComponent(td: Element): PrimeComponent? {
        val name = td
            .selectFirst("[data-param-name]")
            ?.attr("data-param-name")
            ?.trim()
            ?: return null

        if (!name.contains("Prime"))
            return null

        val quantity = td.ownText()
            .trim()
            .replace(",", "")
            .toIntOrNull() ?: 1

        val value = name.removePrefix("Prime ").trim()

        return parsePrimeValue(value, quantity)
    }

    private fun parsePrimeValue(value: String, quantity: Int): PrimeComponent {

        val normalized = value
            .uppercase()
            .replace(' ', '_')

        val primePart = PrimePart.entries
            .firstOrNull { it.name == normalized }

        return if (primePart != null) {
            PrimeComponent(
                id = normalizePrimeSetId("$primeName $value"),
                part = primePart,
                quantity = quantity
            )
        } else {
            PrimeComponent(
                id = normalizePrimeSetId(value),
                part = PrimePart.PRIME_SET,
                quantity = quantity
            )
        }
    }

    private fun normalizePrimeSetId(name: String): String =
        name.lowercase().replace(' ', '_')
}