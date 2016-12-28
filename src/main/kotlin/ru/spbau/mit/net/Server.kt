package ru.spbau.mit.net

/**
 * Class is responsible for:
 * - server initialization
 * - client acceptance
 */

interface Server {
    fun start()
    fun stop()
    fun shutdown()

    fun getClient(): Client?

    fun setOnConnected(callback: () -> Unit)
    fun setOnDisconnected(callback: () -> Unit)
}