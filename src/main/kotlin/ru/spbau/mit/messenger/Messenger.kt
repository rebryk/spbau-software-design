package ru.spbau.mit.messenger

import java.io.DataInputStream
import java.io.DataOutputStream

interface Messenger {
    fun receiveMessage(inputStream: DataInputStream)
    fun sendMessage(outputStream: DataOutputStream, message: Message)
    fun sendSystemMessage(message: String)

    fun setOnMessageReceived(callback: (Message) -> Unit)
    fun setOnSystemMessageReceived(callback: (String) -> Unit)
    fun setOnMessageSent(callback: (Message) -> Unit)
}