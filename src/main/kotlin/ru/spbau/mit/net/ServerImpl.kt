package ru.spbau.mit.net

import org.apache.logging.log4j.LogManager
import ru.spbau.mit.messenger.Messenger
import java.net.ServerSocket
import java.net.SocketTimeoutException

class ServerImpl: Server {
    private val logger = LogManager.getLogger("ServerImpl")

    private val messenger: Messenger
    private val socket: ServerSocket

    private @Volatile var listener: Thread
    private @Volatile var client: ClientImpl? = null
    private @Volatile var isRunning: Boolean = false

    private var onConnected: (() -> Unit)? = null
    private var onDisconnected: (() -> Unit)? = null

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
                                client?.setOnDisconnected({
                                    client = null
                                    onDisconnected?.invoke()
                                })
                                client?.start()
                                onConnected?.invoke()
                            }
                        } catch (e: SocketTimeoutException) {
                        } catch (e: Exception) {
                            logger.debug("Listener stopped!")
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

    override fun getClient(): ClientImpl? {
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
            logger.error("Failed to stop server!")
        }
    }

    /**
     * Methods to set callbacks
     */

    override fun setOnConnected(callback: () -> Unit) {
        onConnected = callback
    }

    override fun setOnDisconnected(callback: () -> Unit) {
        onDisconnected = callback
    }
}