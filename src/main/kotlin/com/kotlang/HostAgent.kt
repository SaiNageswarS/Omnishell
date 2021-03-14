package com.kotlang

import com.kotlang.omnishell.AutoCompleteServiceGrpcKt
import com.kotlang.omnishell.FileSystemManagerGrpcKt
import com.kotlang.omnishell.HistoryManagerGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.File

val hostAgent = HostAgent("localhost", 50051)

class HostAgent(host: String, port: Int) {
    private val channel: ManagedChannel

    val historyManagerClient: HistoryManagerGrpcKt.HistoryManagerCoroutineStub
    val autoCompleteClient: AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub
    val fileSystemClient: FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub

    init {
        //copy host agent to home folder
        val hostAgentUrl = object {}.javaClass.getResource("/hostManager/linux/OmnishellProcessManager")
        Runtime.getRuntime().exec(hostAgentUrl.file)

        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
        historyManagerClient = HistoryManagerGrpcKt.HistoryManagerCoroutineStub(channel)
        autoCompleteClient = AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub(channel)
        fileSystemClient = FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub(channel)
    }
}