package com.kotlang

import com.kotlang.omnishell.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.net.URL

val hostAgent = HostAgent("localhost", 50051)

class HostAgent(host: String, port: Int) {
    private val channel: ManagedChannel

    val historyManagerClient: HistoryManagerGrpcKt.HistoryManagerCoroutineStub
    val autoCompleteClient: AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub
    val fileSystemClient: FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub
    val commandExecutionClient: CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub
    val environmentClient: EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub

    private fun getHostAgentUrl(): String {
        val os = System.getProperty("os.name")
        return when {
            os.indexOf("win") >= 0 -> "./hostManager/windows/OmnishellProcessManager.exe"
            os.indexOf("mac") >= 0 -> "./hostManager/mac/OmnishellProcessManager"
            else -> "./hostManager/linux/OmnishellProcessManager"
        }
    }

    init {
        //copy host agent to home folder
        Runtime.getRuntime().exec(getHostAgentUrl())

        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        historyManagerClient = HistoryManagerGrpcKt.HistoryManagerCoroutineStub(channel)
        autoCompleteClient = AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub(channel)
        fileSystemClient = FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub(channel)
        commandExecutionClient = CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub(channel)
        environmentClient = EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub(channel)
    }
}