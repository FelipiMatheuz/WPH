package remote

import logging.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HtmlDownloader {
    fun download(url: String): Document {

        val pageName = url.replace("/Prime", " Prime")
            .takeLastWhile { it != '/' }
            .replace("w ", "")

        Logger.info("Downloader","Downloading HTML page \"$pageName\"...")
        return Jsoup
            .connect(url)
            .userAgent("prime-hunt-data")
            .timeout(60_000)
            .maxBodySize(0)
            .get()
    }
}
