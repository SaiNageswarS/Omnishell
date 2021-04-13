package com.kotlang

import com.google.protobuf.StringValue
import com.kotlang.omnishell.*
import com.kotlang.remoting.RemoteTargetManager
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking

class HostAgent(remoteHost: RemoteTargetManager) {
    private val channel: ManagedChannel

    val historyManagerClient: HistoryManagerGrpcKt.HistoryManagerCoroutineStub
    val autoCompleteClient: AutoCompleteServiceGrpcKt.AutoCompleteServiceCoroutineStub
    val fileSystemClient: FileSystemManagerGrpcKt.FileSystemManagerCoroutineStub
    val commandExecutionClient: CommandExecutionServiceGrpcKt.CommandExecutionServiceCoroutineStub
    val environmentClient: EnvironmentManagerGrpcKt.EnvironmentManagerCoroutineStub

    val process: Process = remoteHost.runRemoteHostManager()

    init {
        channel = ManagedChannelBuilder.forAddress(remoteHost.host, remoteHost.port).usePlaintext().build()
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
