package ru.spbau.mit

import java.awt.Color
import java.awt.EventQueue
import java.io.DataInputStream
import java.io.DataOutputStream
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

data class Message(val owner: String, val text: String)

class MessengerImpl : Messenger {
    private val chat: JTextPane?

    private val nameStyle: SimpleAttributeSet
    private val textStyle: SimpleAttributeSet
    private val infoStyle: SimpleAttributeSet

    constructor(chat: JTextPane?) {
        this.chat = chat

        this.nameStyle = SimpleAttributeSet()
        StyleConstants.setFontFamily(nameStyle, "Trebuchet MS")
        StyleConstants.setBold(nameStyle, true)
        StyleConstants.setFontSize(nameStyle, 12)

        this.textStyle = SimpleAttributeSet()
        StyleConstants.setFontFamily(textStyle, "Trebuchet MS")
        StyleConstants.setForeground(textStyle, Color.BLACK)
        StyleConstants.setFontSize(textStyle, 12)

        this.infoStyle = SimpleAttributeSet()
        StyleConstants.setFontFamily(infoStyle, "Trebuchet MS")
        StyleConstants.setItalic(infoStyle, true)
        StyleConstants.setForeground(infoStyle, Color.DARK_GRAY)
        StyleConstants.setFontSize(infoStyle, 11)
        StyleConstants.setAlignment(infoStyle, StyleConstants.ALIGN_CENTER)
    }

    override fun receiveMessage(inputStream: DataInputStream) {
        val message = Message(inputStream.readUTF(), inputStream.readUTF())
        EventQueue.invokeLater {
            StyleConstants.setForeground(nameStyle, Color(30, 54, 191))

            chat?.styledDocument.let {
                it?.insertString(it.length, "%s\n".format(message.owner), nameStyle)
                it?.insertString(it.length, "%s\n".format(message.text), textStyle)
            }
        }

        System.out.println("%s: %s".format(message.owner, message.text))
    }

    override fun sendMessage(outputStream: DataOutputStream, message: Message) {
        outputStream.writeUTF(message.owner)
        outputStream.writeUTF(message.text)

        EventQueue.invokeLater {
            StyleConstants.setForeground(nameStyle, Color(30, 191, 43))

            chat?.styledDocument.let {
                it?.insertString(it.length, "%s\n".format(message.owner), nameStyle)
                it?.insertString(it.length, "%s\n".format(message.text), textStyle)
            }
        }

        System.out.println("%s: %s".format(message.owner, message.text))
    }

    override fun showText(text: String) {
        EventQueue.invokeLater {
            chat?.styledDocument.let {
                it?.insertString(it.length, "%s\n".format(text), infoStyle)
            }
        }
    }
}