package com.kotlang

import com.google.protobuf.StringValue
import com.kotlang.omnishell.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking

class HostAgent(host: String, port: Int) {
    private val channel: ManagedChannel

    val historyManagerClient: HistoryManagerGrpcKt.HistoryManagerCoroutineStub
    val autoCompleteClient: AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub
    val fileSystemClient: FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub
    val commandExecutionClient: CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub
    val environmentClient: EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub

    val process: Process

    private fun getHostAgentUrl(): String {
        val os = System.getProperty("os.name").toLowerCase()
        return when {
            os.indexOf("win") >= 0 -> "$hostManagerPath/hostManager/windows/OmnishellProcessManager.exe"
            os.indexOf("mac") >= 0 -> {
                val hostAgentUrl = "$hostManagerPath/hostManager/mac/OmnishellProcessManager"
                Runtime.getRuntime().exec("chmod +x $hostAgentUrl")
                hostAgentUrl
            }
            else -> {
                val hostAgentUrl = "$hostManagerPath/hostManager/linux/OmnishellProcessManager"
                Runtime.getRuntime().exec("chmod +x $hostAgentUrl")
                hostAgentUrl
            }
        }
    }

    init {
        val hostAgentUrl = getHostAgentUrl()
        process = Runtime.getRuntime().exec("$hostAgentUrl :$port $hostManagerPath/hostManager/app.log")

        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        historyManagerClient = HistoryManagerGrpcKt.HistoryManagerCoroutineStub(channel)
        autoCompleteClient = AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub(channel)
        fileSystemClient = FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub(channel)
        commandExecutionClient = CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub(channel)
        environmentClient = EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub(channel)
    }

    fun getHome(): String {
        return runBlocking {
            fileSystemClient.getHome(StringValue.getDefaultInstance()).value
        }
    }
}
