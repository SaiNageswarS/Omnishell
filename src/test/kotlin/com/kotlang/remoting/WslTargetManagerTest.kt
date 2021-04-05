package com.kotlang.remoting

import org.junit.jupiter.api.Test

class WslTargetManagerTest {
    @Test
    fun testGetWslUrl() {
        val wslTargetMgr = WslTargetManager("ubuntu")
        val windowsPath = wslTargetMgr.getHostAgentUrl("linux")
        val wslPath = wslTargetMgr.getWslPath(windowsPath)
        println(wslPath)
    }
}