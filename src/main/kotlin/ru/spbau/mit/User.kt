package ru.spbau.mit

import org.apache.logging.log4j.LogManager
import ru.spbau.mit.net.ClientImpl
import ru.spbau.mit.messenger.Messenger
import ru.spbau.mit.net.ServerImpl

class User {
    private val logger = LogManager.getLogger("User")

    private var name: String
    private val messenger: Messenger

    private val server: ServerImpl
    private val client: ClientImpl

    constructor(name: String, messenger: Messenger, port: Int = 0) {
        this.name = name

        this.messenger = messenger

        this.server = ServerImpl(port)
        this.server.setOnConnected { onUserConnectCallback() }
        this.server.setOnDisconnected { setUserDisconnectCallback() }
        this.server.setOnMessageReceived { messenger.receiveMessage(it) }
        this.server.setOnStreamObserverUpdated { messenger.setStream(it) }

        this.client = ClientImpl()
        this.client.setOnConnected { onConnectCallback() }
        this.client.setOnConnectionFailed { onConnectFailedCallback() }
        this.client.setOnDisconnected { onDisconnectCallback() }
        this.client.setOnMessageReceived { messenger.receiveMessage(it) }
        this.client.setOnStreamObserverUpdated { messenger.setStream(it) }
    }

    /**
     * Updates the current user's name
     */
    fun setName(name: String) {
        this.name = name
        messenger.sendSystemMessage("Your name has changed to $name")
        logger.debug("User has changed name to $name")
    }

    /**
     * Sends text message through the network
     * @param text message text
     */
    fun sendMessage(text: String) {
        messenger.sendMessage(name, text)
    }

    /**
     * Starts the server
     */
    fun start() {
        server.start()
    }

    /**
     * Shuts down the server, disconnects the client
     * Call this method only once at the end
     */
    fun stop() {
        server.shutdown()
        client.disconnect()
    }

    /**
     * Stops the server and connects to the given address
     * @param host server hostname
     * @param port server port
     */
    fun connect(host: String, port: Int) {
        server.stop()
        client.connect(host, port)
    }

    /**
     * Disconnects the client, starts the server
     */
    fun disconnect() {
        client.disconnect()
        server.start()
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
        messenger.sendTypingStatus(false)
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