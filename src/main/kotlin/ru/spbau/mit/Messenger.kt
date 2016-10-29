package ru.spbau.mit

import java.io.DataInputStream
import java.io.DataOutputStream

interface Messenger {
    fun receiveMessage(inputStream: DataInputStream)
    fun sendMessage(outputStream: DataOutputStream, message: Message)
    fun showText(text: String)
}