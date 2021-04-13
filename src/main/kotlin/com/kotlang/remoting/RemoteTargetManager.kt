package com.kotlang.remoting

import com.kotlang.hostManagerPath

abstract class RemoteTargetManager {
    abstract fun runRemoteHostManager(): Process

    abstract val port: Int
    abstract val host: String
    abstract val os: String

    fun getHostAgentUrl(os: String): String {
        return when {
            os.indexOf("win") >= 0 -> "$hostManagerPath/hostManager/windows/OmnishellProcessManager.exe"
            os.indexOf("mac") >= 0 -> {
                val hostAgentUrl = "$hostManagerPath/hostManager/mac/OmnishellProcessManager"
                hostAgentUrl
            }
            else -> {
                val hostAgentUrl = "$hostManagerPath/hostManager/linux/OmnishellProcessManager"
                hostAgentUrl
            }
        }
    }
}
