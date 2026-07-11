package remote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HtmlDownloader {
    fun download(url: String): Document {
        println("Downloading HTML ${url.substring(url.replace("/Prime", " Prime").lastIndexOf("/") + 1)}...")
        return Jsoup
            .connect(url)
            .userAgent("prime-hunt-data")
            .timeout(30_000)
            .get()
    }
}
