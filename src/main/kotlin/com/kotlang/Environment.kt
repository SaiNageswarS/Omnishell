package com.kotlang

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Files
import java.nio.file.Path

object Environment {
    private val mapper = jacksonObjectMapper()
    private val envConfigFolder = Path.of(System.getProperty("user.home") + "/config")
    private val envConfigFile = Path.of(envConfigFolder.toString(), "OmnishellEnv.json")

    private val environment = mutableMapOf<String, String>()

    init {
        Files.createDirectories(envConfigFolder)
        if (Files.notExists(envConfigFile)) {
            //write default config to env file
            val procBuilder = ProcessBuilder("/bin/sh", "-c", "date")
            environment.putAll(procBuilder.environment())
            Files.writeString(envConfigFile, mapper.writeValueAsString(environment))
        }
    }

    fun getEnvironment(): Map<String, String> {
        if (environment.isEmpty()) {
            environment.putAll(mapper.readValue(envConfigFile.toFile()))
        }
        return environment
    }
}
