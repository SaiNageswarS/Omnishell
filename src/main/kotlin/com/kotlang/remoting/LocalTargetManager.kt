package com.kotlang.remoting

import com.kotlang.hostManagerPath
import com.kotlang.util.PortUtil

class LocalTargetManager: RemoteTargetManager() {
    private val availablePort = PortUtil.getFreePort()
    private val localOs = System.getProperty("os.name").toLowerCase()

    override fun runRemoteHostManager(): Process {
        val hostAgentUrl = getHostAgentUrl(localOs)

        if (localOs.indexOf("mac") >= 0 || localOs.indexOf("linux") >= 0) {
            val p = Runtime.getRuntime().exec("chmod +x $hostAgentUrl")
            p.waitFor()
        }

        return Runtime.getRuntime().exec("$hostAgentUrl :$availablePort $hostManagerPath/hostManager/app.log")
    }

    override val port: Int
        get() = availablePort
    override val host: String
        get() = "localhost"
    override val os: String
        get() = localOs
}