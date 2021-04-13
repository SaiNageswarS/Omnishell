package com.kotlang.remoting

import com.kotlang.hostManagerPath
import com.kotlang.util.PortUtil
import java.util.*

class WslTargetManager(private val wslTarget: String): RemoteTargetManager() {
    companion object {
        fun listWsl(): List<String> {
            val os = System.getProperty("os.name").toLowerCase()
            val result = mutableListOf<String>()
            if (os.indexOf("win") >= 0) {
                val process = Runtime.getRuntime().exec("wsl --list")
                process.waitFor()
                val sc = Scanner(process.inputStream)
                sc.nextLine()
                while (sc.hasNextLine()) {
                    var outLine = sc.nextLine()

                    outLine = outLine.replace(0.toChar().toString(), "")
                    outLine = outLine.removeSuffix("(Default)").trim()
                    if (outLine.isNotEmpty()) {
                        result.add(outLine)
                    }
                }
            }
            return result
        }
    }

    fun getWslPath(windowsPath: String): String {
        var process = Runtime.getRuntime().exec("wsl wslpath $windowsPath")
        process.waitFor()
        val sc = Scanner(process.inputStream)
        //get wsl path from command output
        val wslPath = sc.nextLine().replace(0.toChar().toString(), "")

        //make the file executable
        process = Runtime.getRuntime().exec("$wslTarget run chmod +x $wslPath")
        process.waitFor()
        return wslPath
    }

    private val availablePort = PortUtil.getFreePort()
    override fun runRemoteHostManager(): Process {
        val hostAgentUrl = getWslPath(getHostAgentUrl("linux").replace("\\", "/"))
        val hostManagerPathWsl = getWslPath(hostManagerPath.toString().replace("\\", "/"))

        return Runtime.getRuntime().exec("$wslTarget run $hostAgentUrl :$availablePort $hostManagerPathWsl/hostManager/appWsl.log")
    }

    override val port: Int
        get() = availablePort
    override val host: String
        get() = "localhost"
    //wsl is always linux
    override val os: String
        get() = "linux"
}