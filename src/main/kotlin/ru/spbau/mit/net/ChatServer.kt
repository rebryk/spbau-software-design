package ru.spbau.mit.net

import io.grpc.stub.StreamObserver
import ru.spbau.mit.Message

/**
 * ChatServer is responsible for starting server and client acceptance
 * It has the set of callbacks to process different events
 */

interface ChatServer {
    fun start()
    fun stop()
    fun shutdown()

    fun setOnConnected(callback: () -> Unit)
    fun setOnDisconnected(callback: () -> Unit)
    fun setOnMessageReceived(callback: (Message) -> Unit)
    fun setOnStreamObserverUpdated(callback: (StreamObserver<Message>) -> Unit)
}