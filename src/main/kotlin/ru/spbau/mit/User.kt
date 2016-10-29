package ru.spbau.mit

import java.net.SocketAddress

open class User {
    private var name: String
    private val messenger: Messenger

    private val server: ServerImpl
    private val client: ClientImpl

    constructor(name: String, messenger: Messenger, port: Int = 0) {
        this.name = name
        this.messenger = messenger

        this.server = ServerImpl(messenger, port)
        this.server.setOnConnectCallback { onUserConnectCallback() }
        this.server.setOnDisconnectCallback { setUserDisconnectCallback()}

        this.client = ClientImpl(messenger)
        this.client.setOnConnectCallback { onConnectCallback() }
        this.client.setOnConnectFailedCallback { onConnectFailedCallback() }
        this.client.setOnDisconnectCallback { onDisconnectCallback() }
    }

    fun setName(name: String) {
        this.name = name
        messenger.showText("Your name has changed to %s".format(name))
    }

    fun sendMessage(text: String) {
        val message = Message(name, text)
        server.getClient()?.sendMessage(message) ?: client.sendMessage(message)
    }

    fun start() {
        server.start()
    }

    /**
     * Method closes all sockets and stops all threads.
     * Call this method only once at the end.
     */
    fun stop() {
        server.stop()
        server.shutdown()
        client.disconnect()
    }

    /**
     * Method stops the server and connects to the given address
     */
    fun connect(address: SocketAddress) {
        server.stop()
        client.connect(address)
        client.start()
    }

    fun disconnect() {
        client.disconnect()
    }

    /**
     * CALLBACKS
     */

    private fun onUserConnectCallback() {
        messenger.showText("User connected!")
    }

    private fun setUserDisconnectCallback() {
        messenger.showText("User disconnected!")
    }

    private fun onConnectCallback() {
        messenger.showText("You connected!")
    }

    private fun onConnectFailedCallback() {
        messenger.showText("Failed to connect!")
        server.start()
    }

    private fun onDisconnectCallback() {
        messenger.showText("You disconnected!")
        server.start()
    }
}