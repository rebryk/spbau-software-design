package ru.spbau.mit.net

import io.grpc.stub.StreamObserver
import ru.spbau.mit.Message

interface ChatClient {
    fun connect(host: String, port: Int)
    fun disconnect()

    fun setOnConnected(callback: () -> Unit)
    fun setOnDisconnected(callback: () -> Unit)
    fun setOnConnectionFailed(callback: () -> Unit)
    fun setOnMessageReceived(callback: (Message) -> Unit)
    fun setOnStreamObserverUpdated(callback: (StreamObserver<Message>) -> Unit)
}
