package ru.spbau.mit.messenger

import io.grpc.stub.StreamObserver
import org.apache.logging.log4j.LogManager
import ru.spbau.mit.Message
import ru.spbau.mit.TextMessage
import ru.spbau.mit.TypingStatus

class MessengerImpl : Messenger {
    private val logger = LogManager.getLogger("MessengerImpl")

    private var stream: StreamObserver<Message>? = null

    private var onMessageReceived: (Message) -> Unit = {}
    private var onSystemMessageReceived: (String) -> Unit = {}
    private var onMessageSent: (Message) -> Unit = {}

    /**
     * Receives message from the network
     * @param message received message
     */
    override fun receiveMessage(message: Message) {
        onMessageReceived.invoke(message)
        logger.debug("Message sent: $message")
    }

    /**
     * Shows system message to the user
     * @param message system message
     */
    override fun sendSystemMessage(message: String) {
        onSystemMessageReceived.invoke(message)
        logger.debug("System message received: $message")
    }

    /**
     * Sends the given message through the network
     * @param owner user nickname
     * @param text message text
     */
    override fun sendMessage(owner: String, text: String) {
        val textMessage = TextMessage.newBuilder().setOwner(owner).setText(text).build()
        val message = Message.newBuilder().setTextMessage(textMessage).build()
        send(message)
    }

    /**
     * Sends typing status to the other user
     * @param typing typing status
     */
    override fun sendTypingStatus(typing: Boolean) {
        val status = TypingStatus.newBuilder().setTyping(typing).build()
        val message = Message.newBuilder().setTypingStatus(status).build()
        send(message)
    }


    /**
     * Sets current stream observer
     * @param stream current server/client stream observer
     */
    override fun setStream(stream: StreamObserver<Message>?) {
        this.stream = stream
    }

    /**
     * Sends the given message through the network
     * @param message message to send
     */
    private @Synchronized fun send(message: Message) {
        stream?.let {
            try {
                onMessageSent.invoke(message)
                it.onNext(message)
            } catch (e: Exception) {
                logger.error("$e")
            }
        }
    }

    /**
     * Methods to set callbacks
     */

    override fun setOnMessageReceived(callback: (Message) -> Unit) {
        onMessageReceived = callback
    }

    override fun setOnSystemMessageReceived(callback: (String) -> Unit) {
        onSystemMessageReceived = callback
    }

    override fun setOnMessageSent(callback: (Message) -> Unit) {
        onMessageSent = callback
    }
}