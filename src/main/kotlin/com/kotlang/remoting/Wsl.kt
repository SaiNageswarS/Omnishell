package com.kotlang.remoting

import java.util.*

object Wsl {
    fun listWsl(): List<String> {
        val os = System.getProperty("os.name").toLowerCase()
        val result = mutableListOf<String>()
        if (os.indexOf("win") >= 0) {
            val process = Runtime.getRuntime().exec("wsl --list")
            process.waitFor()
            val sc = Scanner(process.inputStream)
            sc.nextLine()
            while (sc.hasNextLine()) {
                val outLine = sc.nextLine().replace(0.toChar().toString(), "")
                if (outLine.isNotEmpty()) {
                    result.add(outLine)
                }
            }
        }
        return result
    }

    fun runHostManager(wslDistro: String) {

    }
}