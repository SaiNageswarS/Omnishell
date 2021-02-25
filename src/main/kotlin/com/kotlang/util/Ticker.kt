package com.kotlang.util

import com.kotlang.CommandState

/**
 * Used to produce ticks for UI events.
 */
class Ticker() {
    lateinit var notify: (Int) -> Unit

    private var currentTick = 0
        set(value) {
            field = value
            notify(field)
        }
    private var pollingThread: Thread? = null
    @Volatile
    private var isRunning: Boolean = true


    //Will be called only once per instance
    fun poll() {
        if (pollingThread == null) {
            pollingThread = Thread {
                while (isRunning) {
                    Thread.sleep(500)
                    currentTick += 1
                }
            }
            pollingThread!!.start()
        }
    }

    fun stop() {
        isRunning = false
    }
}