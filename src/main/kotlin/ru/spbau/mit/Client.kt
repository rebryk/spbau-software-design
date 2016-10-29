package ru.spbau.mit

import java.net.SocketAddress

interface Client {
    fun start()
    fun connect(address: SocketAddress)
    fun disconnect()
    fun sendMessage(message: Message)
}
