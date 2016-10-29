package ru.spbau.mit

import java.net.ServerSocket
import java.net.SocketTimeoutException

class ServerImpl: Server {
    private val messenger: Messenger
    private val socket: ServerSocket

    private @Volatile var listener: Thread
    private @Volatile var client: ClientImpl? = null
    private @Volatile var isRunning: Boolean = false

    private var onConnect: (() -> Unit)? = null
    private var onDisconnect: (() -> Unit)? = null

    /**
     * This thread listens the socket for new connections
     */
    private inner class Listener : Thread {
        private val DELAY: Long = 100

        constructor() { }

        override fun run() {
            while (!Thread.interrupted()) {
                if (isRunning && client == null) {
                    synchronized(this@ServerImpl) {
                        try {
                            socket.accept().let {
                                client = ClientImpl(messenger, it)
                                client?.setOnDisconnectCallback({
                                    client = null
                                    onDisconnect?.invoke()
                                })
                                client?.start()
                                onConnect?.invoke()
                            }
                        } catch (e: SocketTimeoutException) {
                        } catch (e: Exception) {
                            return@run
                        }
                    }

                    try {
                        Thread.sleep(DELAY)
                    } catch (e: InterruptedException) {
                        return
                    }
                }
            }
        }
    }

    constructor(messenger: Messenger, port: Int) {
        this.messenger = messenger
        this.socket = ServerSocket(port)
        this.socket.soTimeout = 100
        this.listener = Listener()
        this.listener.start()
    }

    fun getClient(): ClientImpl? {
        return client
    }

    override @Synchronized fun start() {
        if (!socket.isClosed) {
            isRunning = true
        }
    }

    override @Synchronized fun stop() {
        isRunning = false
        client?.disconnect()
    }

    override fun shutdown() {
        try {
            listener.interrupt()

            if (!socket.isClosed) {
                socket.close()
            }
        } catch (e: Exception) {
            System.err.println("Failed to stop server!")
        }
    }

    fun setOnConnectCallback(callback: () -> Unit) {
        onConnect = callback
    }

    fun setOnDisconnectCallback(callback: () -> Unit) {
        onDisconnect = callback
    }
}