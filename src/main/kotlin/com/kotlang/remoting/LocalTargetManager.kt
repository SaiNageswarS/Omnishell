package com.kotlang.remoting

import com.kotlang.hostManagerPath
import com.kotlang.util.PortUtil

class LocalTargetManager: RemoteTargetManager() {
    private val availablePort = PortUtil.getFreePort()
    override fun runRemoteHostManager(): Process {
        val os = System.getProperty("os.name").toLowerCase()
        val hostAgentUrl = getHostAgentUrl(os)

        if (os.indexOf("mac") >= 0 || os.indexOf("linux") >= 0) {
            val p = Runtime.getRuntime().exec("chmod +x $hostAgentUrl")
            p.waitFor()
        }

        return Runtime.getRuntime().exec("$hostAgentUrl :$availablePort $hostManagerPath/hostManager/app.log")
    }

    override val port: Int
        get() = availablePort
    override val host: String
        get() = "localhost"
}