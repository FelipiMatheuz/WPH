package normalizer

import java.io.File
import java.security.MessageDigest

fun sha256(file: File): String {

    val digest = MessageDigest.getInstance("SHA-256")

    val hash = digest.digest(file.readBytes())

    return hash.joinToString("") {
        "%02x".format(it)
    }
}