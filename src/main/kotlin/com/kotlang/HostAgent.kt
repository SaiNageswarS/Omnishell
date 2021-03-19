package com.kotlang

import com.google.protobuf.StringValue
import com.kotlang.omnishell.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking

val hostAgent = HostAgent("localhost", 50051)

class HostAgent(host: String, port: Int) {
    private val channel: ManagedChannel

    val historyManagerClient: HistoryManagerGrpcKt.HistoryManagerCoroutineStub
    val autoCompleteClient: AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub
    val fileSystemClient: FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub
    val commandExecutionClient: CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub
    val environmentClient: EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub

    val process: Process

    private fun getHostAgentUrl(): String {
        val os = System.getProperty("os.name")
        return when {
            os.indexOf("Win") >= 0 -> "./hostManager/windows/OmnishellProcessManager.exe"
            os.indexOf("mac") >= 0 -> "./hostManager/mac/OmnishellProcessManager"
            else -> "./hostManager/linux/OmnishellProcessManager"
        }
    }

    init {
        //copy host agent to home folder
        process = Runtime.getRuntime().exec(getHostAgentUrl())

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