package ru.spbau.mit

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.net.Socket
import java.net.SocketAddress

class ClientImpl: Client {
    private var socket: Socket
    private val messenger: Messenger

    private var onConnect: (() -> Unit)? = null
    private var onConnectFailed: (() -> Unit)? = null
    private var onDisconnect: (() -> Unit)? = null

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
            System.err.println("Failed to send message!")
        }
    }

    /**
     * Method creates new socket, connects to the given address and calls the callback
     */
    override fun connect(address: SocketAddress) {
        try {
           socket = Socket()
           socket.connect(address)
           onConnect?.invoke()
        } catch (e: Exception) {
            System.err.println("Connection failed!")
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
            System.err.println("Disconnection failed!")
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
                System.out.println("Disconnected.")
            } catch (e: Exception) {
                System.err.println("Failed to receive message!")
            } finally {
                disconnect()
                onDisconnect?.invoke()
            }
        }).start()
    }

    fun setOnConnectCallback(callback: () -> Unit) {
        onConnect = callback
    }

    fun setOnDisconnectCallback(callback: () -> Unit) {
        onDisconnect = callback
    }

    fun setOnConnectFailedCallback(callback: () -> Unit) {
        onConnectFailed = callback
    }
}