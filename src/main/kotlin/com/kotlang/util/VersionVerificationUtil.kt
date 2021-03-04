package com.kotlang.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class VersionVerificationUtil(
    private val httpClient: HttpClient = HttpClient.newHttpClient(),
    private val currentVersion: VersionString = VersionString("0.0.3"),
    private val os: String = System.getProperty("os.name").split(" ").first()
) {
    private val mapper = jacksonObjectMapper()

    private fun getReleases(): List<Map<String, Any>> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/SaiNageswarS/Omnishell/releases"))
            .timeout(Duration.ofSeconds(30))
            .header("Accept", "application/vnd.github.v3+json")
            .GET().build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        return mapper.readValue(response.body())
    }

    fun checkIsOldVersion(): Boolean {
        try {
            val releases = getReleases()
            val osTags = releases.map { it["tag_name"].toString() }
                .filter { it.startsWith(os) }
            val availableVersion = VersionString(osTags[0].split("-").last())
            return currentVersion < availableVersion
        } catch (e: Exception) {
            return false
        }
    }
}