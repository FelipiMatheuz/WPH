package remote

import logging.LogMetadata
import logging.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HtmlDownloader {
    fun download(url: String): Document {

        val pageName = url.replace("/Prime", " Prime")
            .takeLastWhile { it != '/' }
            .replace("w ", "")

        Logger.info(
            "Downloading HTML page \"$pageName\"...", null,
            listOf(LogMetadata("URL", url))
        )

        return Jsoup
            .connect(url)
            .userAgent("prime-tracker-data")
            .timeout(60_000)
            .maxBodySize(0)
            .get()
    }
}
