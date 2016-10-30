package ru.spbau.mit

import org.apache.logging.log4j.LogManager
import ru.spbau.mit.net.ClientImpl
import ru.spbau.mit.messenger.Message
import ru.spbau.mit.messenger.Messenger
import ru.spbau.mit.net.ServerImpl
import java.net.SocketAddress

class User {
    private val logger = LogManager.getLogger("User")

    private var name: String
    private val messenger: Messenger

    private val server: ServerImpl
    private val client: ClientImpl

    constructor(name: String, messenger: Messenger, port: Int = 0) {
        this.name = name
        this.messenger = messenger

        this.server = ServerImpl(messenger, port)
        this.server.setOnConnected { onUserConnectCallback() }
        this.server.setOnDisconnected { setUserDisconnectCallback()}

        this.client = ClientImpl(messenger)
        this.client.setOnConnected { onConnectCallback() }
        this.client.setOnConnectionFailed { onConnectFailedCallback() }
        this.client.setOnDisconnected { onDisconnectCallback() }
    }

    fun setName(name: String) {
        this.name = name
        messenger.sendSystemMessage("Your name has changed to %s".format(name))
        logger.debug("User has changed name to %s".format(name))
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
     * Callbacks
     */

    private fun onUserConnectCallback() {
        messenger.sendSystemMessage("User connected!")
        logger.debug("User connected!")
    }

    private fun setUserDisconnectCallback() {
        messenger.sendSystemMessage("User disconnected!")
        logger.debug("User disconnected!")
    }

    private fun onConnectCallback() {
        messenger.sendSystemMessage("You connected!")
        logger.debug("You connected!")
    }

    private fun onConnectFailedCallback() {
        messenger.sendSystemMessage("Failed to connect!")
        logger.debug("Failed to connect!")
        server.start()
    }

    private fun onDisconnectCallback() {
        messenger.sendSystemMessage("You disconnected!")
        logger.debug("You disconnected!")
        server.start()
    }
}