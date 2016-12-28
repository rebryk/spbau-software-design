package ru.spbau.mit.messenger

import io.grpc.stub.StreamObserver
import ru.spbau.mit.Message

/**
 * Class is responsible for:
 * - writing messages (text/typing status)
 * - reading messages (text/typing status)
 * - system message sending (messages for user)
 */

interface Messenger {
    fun receiveMessage(message: Message)
    fun sendSystemMessage(message: String)

    fun sendMessage(owner: String, text: String)
    fun sendTypingStatus(typing: Boolean)

    fun setStream(stream: StreamObserver<Message>?)

    fun setOnMessageReceived(callback: (Message) -> Unit)
    fun setOnSystemMessageReceived(callback: (String) -> Unit)
    fun setOnMessageSent(callback: (Message) -> Unit)
}