package extractor

import model.domain.prime.PrimeComponent
import model.domain.prime.PrimePart
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class PrimeSetDetailsExtractor(private val primeName: String) {
    fun extract(document: Document): List<PrimeComponent>? {
        val table = document.selectFirst("table.foundrytable")
            ?: return null

        // Primeira linha após o cabeçalho da tabela
        val firstRequirementRow = table.select("tr")[1]

        return firstRequirementRow
            .select("td")
            .mapNotNull(::parseComponent)
            .groupBy { it.part to it.id }
            .map { (_, components) ->
                components.first().copy(quantity = components.size)
            }
    }

    private fun parseComponent(td: Element): PrimeComponent? {
        val name = td
            .selectFirst("[data-param-name]")
            ?.attr("data-param-name")
            ?.trim()
            ?: return null

        // Ignore common resources
        if (!name.contains("Prime"))
            return null

        val value = name.removePrefix("Prime ").trim()

        return parsePrimeValue(value)
    }

    private fun parsePrimeValue(value: String): PrimeComponent {

        val normalized = value
            .uppercase()
            .replace(' ', '_')

        val primePart = PrimePart.entries
            .firstOrNull { it.name == normalized }

        return if (primePart != null) {
            PrimeComponent(
                id = normalizePrimeSetId("$primeName $value"),
                part = primePart,
                quantity = 1
            )
        } else {
            PrimeComponent(
                id = normalizePrimeSetId(value),
                part = PrimePart.PRIME_SET,
                quantity = 1
            )
        }
    }

    private fun normalizePrimeSetId(name: String): String =
        name.lowercase().replace(' ', '_')
}