package ru.spbau.mit.net

import org.apache.logging.log4j.LogManager
import ru.spbau.mit.messenger.Message
import ru.spbau.mit.messenger.Messenger
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.net.Socket
import java.net.SocketAddress

class ClientImpl: Client {
    private val logger = LogManager.getLogger("ClientImpl")

    private var socket: Socket
    private val messenger: Messenger

    private var onConnected: (() -> Unit)? = null
    private var onConnectionFailed: (() -> Unit)? = null
    private var onDisconnected: (() -> Unit)? = null

    constructor(messenger: Messenger, socket: Socket = Socket()) {
        this.messenger = messenger
        this.socket = socket
    }

    /**
     * Method sends the given message to the socket
     */
    override fun sendMessage(message: Message) {
        try {
            if (!socket.isClosed && socket.isConnected) {
                messenger.sendMessage(DataOutputStream(socket.outputStream), message)
            }
        } catch (e: Exception) {
            logger.error("Failed to send message!")
        }
    }

    /**
     * Method creates new socket, connects to the given address and calls the callback
     */
    override fun connect(address: SocketAddress) {
        try {
           socket = Socket()
           socket.connect(address)
           onConnected?.invoke()
        } catch (e: Exception) {
            logger.error("Connection failed!")
        }
    }

    /**
     * Method closes the socket if it's opened
     */
    override fun disconnect() {
        try {
            if (!socket.isClosed) {
                socket.close()
            }
        } catch (e: Exception) {
            logger.error("Disconnection failed!")
        }
    }

    /**
     * Method starts new thread which receives messages
     */
    override fun start() {
        Thread({
            try {
                while (true) {
                    messenger.receiveMessage(DataInputStream(this.socket.inputStream))
                }
            } catch (e: EOFException) {
                logger.debug("Disconnected.")
            } catch (e: Exception) {
                logger.error("Failed to receive message!")
            } finally {
                disconnect()
                onDisconnected?.invoke()
            }
        }).start()
    }

    /**
     *  Methods to set callbacks
     */

    override fun setOnConnected(callback: () -> Unit) {
        onConnected = callback
    }

    override fun setOnDisconnected(callback: () -> Unit) {
        onDisconnected = callback
    }

    override fun setOnConnectionFailed(callback: () -> Unit) {
        onConnectionFailed = callback
    }
}