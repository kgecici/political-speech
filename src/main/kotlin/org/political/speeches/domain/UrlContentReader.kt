package org.political.speeches.domain

import org.political.speeches.FileFormatSecurityException
import org.political.speeches.UrlCannotReachable
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/*
* This class reads given urls and returns content of the csv files as [url] -> [csv Line List]
* It tries to read header of csv over http and if it s not contain line feed until HEADER_LENGTH, throws exception
* In this way, we prevent malicious content loading
*/
class UrlContentReader {
    val HEADER = "Speaker;Topic;Date;Words";
    val HEADER_LENGTH = "Speaker;Topic;Date;Words".length * 2 // * 2 to avoid and whitespace errors

    fun readContents(urls: Set<String>): Map<String,List<String>> {
        val result = mutableMapOf<String, List<String>>()

        for (url in urls) {
            val connection = connectToUrl(url)

            val bufferedReader = connection.inputStream.bufferedReader()

            readAndValidateHeader(bufferedReader)

            // Read the remaining
            val csvLines = mutableListOf<String>()
            csvLines.addAll(bufferedReader.readLines())

            result[url] = csvLines
        }

        return result
    }

    private fun connectToUrl(url: String): HttpURLConnection {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connect()
        } catch (e: IOException) {
            throw UrlCannotReachable("URL cannot reachable. url: ${url}")
        }
        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw UrlCannotReachable("URL not found. url: $url")
        }
        return connection
    }

    private fun readAndValidateHeader(bufferedReader: BufferedReader) {
        // To avoid reading wrong file format, first we are reading just header
        // and check if it is correct, because the file might contain huge data without any line feed
        val header = StringBuilder()
        var totalBytesRead = 0

        while (totalBytesRead < HEADER_LENGTH) {
            val char = bufferedReader.read().toChar()
            if (char == '\n' || char == '\r' || char.code == -1) {
                break
            }
            header.append(char)
            totalBytesRead++
        }
        val cleanedHeader = header.toString().split(";")
            .map { it.trim() }
            .joinToString(";")

        if (cleanedHeader != HEADER) {
            throw FileFormatSecurityException("Header should be $HEADER but it is $cleanedHeader")
        }
    }

}