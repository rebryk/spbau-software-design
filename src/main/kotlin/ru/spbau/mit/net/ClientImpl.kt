package ru.spbau.mit.net

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager
import ru.spbau.mit.Message
import ru.spbau.mit.MessengerGrpc
import java.util.concurrent.TimeUnit

class ClientImpl : ChatClient {
    private val logger = LogManager.getLogger("ClientImpl")

    private var onConnected: () -> Unit = {}
    private var onConnectionFailed: () -> Unit = {}
    private var onDisconnected: () -> Unit = {}
    private var onMessageReceived: (Message) -> Unit = {}
    private var onStreamObserverUpdated: (StreamObserver<Message>) -> Unit = {}

    private var channel: ManagedChannel? = null

    /**
     * Method creates new channel, connects to the given address and calls the callback
     * @param host server hostname
     * @param port server port
     */
    override fun connect(host: String, port: Int) {
        try {
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build()
            val stub = MessengerGrpc.newStub(channel)
            val stream = stub.send(object : StreamObserver<Message> {
                override fun onNext(message: Message) {
                    onMessageReceived.invoke(message)
                }

                override fun onError(error: Throwable) {
                    onDisconnected.invoke()
                    logger.error(error)
                }

                override fun onCompleted() {
                    onDisconnected.invoke()
                    logger.debug("Received all messages.")
                }
            })

            onStreamObserverUpdated.invoke(stream)
            onConnected.invoke()
        } catch (e: Exception) {
            onConnectionFailed.invoke()
            logger.error("Connection failed!")
        }
    }

    /**
     * Shuts down the current channel
     */
    override fun disconnect() {
        try {
            channel?.shutdownNow()
            channel?.awaitTermination(1, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.error("Disconnection failed!")
        }
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

    override fun setOnMessageReceived(callback: (Message) -> Unit) {
        onMessageReceived = callback
    }

    override fun setOnStreamObserverUpdated(callback: (StreamObserver<Message>) -> Unit) {
        onStreamObserverUpdated = callback
    }
}