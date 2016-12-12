package ru.spbau.mit.net

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager
import ru.spbau.mit.Message
import ru.spbau.mit.MessengerGrpc

class ServerImpl : ChatServer, MessengerGrpc.MessengerImplBase {
    private val logger = LogManager.getLogger("ServerImpl")

    private val port: Int
    private var server: Server
    private var isRunning: Boolean = false

    private var stream: StreamObserver<Message>? = null

    private var onConnected: () -> Unit = {}
    private var onDisconnected: () -> Unit = {}
    private var onMessageReceived: (Message) -> Unit = {}
    private var onStreamObserverUpdated: (StreamObserver<Message>) -> Unit = {}

    /**
     * Implements gRPC method of communication
     */
    override fun send(responseObserver: StreamObserver<Message>): StreamObserver<Message> {
        return object : StreamObserver<Message> {
            override fun onNext(message: Message) {
                if (!isRunning || (stream != null && stream != responseObserver)) {
                    responseObserver.onCompleted()
                    return
                }

                if (stream == null) {
                    stream = responseObserver
                    onStreamObserverUpdated(responseObserver)
                    onConnected.invoke()
                }

                onMessageReceived(message)
            }

            override fun onError(error: Throwable) {
                stream = null
                onDisconnected.invoke()
                logger.error(error)
            }

            override fun onCompleted() {
                stream = null
                onDisconnected.invoke()
                responseObserver.onCompleted()
                logger.debug("Received all messages.")
            }
        }
    }

    constructor(port: Int) {
        this.port = port
        server = ServerBuilder.forPort(port).addService(this).build().start()
    }

    /**
     * Starts server
     * Set isRunning flag to true
     */
    override fun start() {
        isRunning = true
    }

    /**
     * Stops server
     * Set isRunning flag to false
     * Calls onDisconnected callback
     */
    override fun stop() {
        isRunning = false
        stream?.let {
            it.onCompleted()
            onDisconnected.invoke()
        }
    }

    /**
     * Shuts down the server
     */
    override fun shutdown() {
        try {
            server.shutdown()
            server.awaitTermination()
            logger.debug("Server shut down")
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

    override fun setOnMessageReceived(callback: (Message) -> Unit) {
        onMessageReceived = callback
    }

    override fun setOnStreamObserverUpdated(callback: (StreamObserver<Message>) -> Unit) {
        onStreamObserverUpdated = callback
    }
}