package ru.spbau.mit.net

interface Server {
    fun start()
    fun stop()
    fun shutdown()

    fun getClient(): Client?

    fun setOnConnected(callback: () -> Unit)
    fun setOnDisconnected(callback: () -> Unit)
}