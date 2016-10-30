package ru.spbau.mit.messenger

import org.apache.logging.log4j.LogManager
import java.io.DataInputStream
import java.io.DataOutputStream

class MessengerImpl : Messenger {
    private val logger = LogManager.getLogger("MessengerImpl")
    private var onMessageReceived: ((Message) -> Unit)? = null
    private var onSystemMessageReceived: ((String) -> Unit)? = null
    private var onMessageSent: ((Message) -> Unit)? = null

    override fun receiveMessage(inputStream: DataInputStream) {
        val message = Message(inputStream.readUTF(), inputStream.readUTF())
        onMessageReceived?.invoke(message)
        logger.debug("Message received: (%s, %s)".format(message.owner, message.text))
    }

    override fun sendMessage(outputStream: DataOutputStream, message: Message) {
        outputStream.writeUTF(message.owner)
        outputStream.writeUTF(message.text)
        onMessageSent?.invoke(message)
        logger.debug("Message sent: (%s, %s)".format(message.owner, message.text))
    }

    override fun sendSystemMessage(message: String) {
        onSystemMessageReceived?.invoke(message)
        logger.debug("System message received: %s".format(message))
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