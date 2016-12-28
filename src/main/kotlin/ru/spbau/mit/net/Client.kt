package ru.spbau.mit.net

import ru.spbau.mit.messenger.Message
import java.net.SocketAddress

/**
 * Class is responsible for:
 * - connecting to the server
 * - messages sending
 */

interface Client {
    fun start()
    fun connect(address: SocketAddress)
    fun disconnect()
    fun sendMessage(message: Message)

    fun setOnConnected(callback: () -> Unit)
    fun setOnDisconnected(callback: () -> Unit)
    fun setOnConnectionFailed(callback: () -> Unit)
}
