package ru.spbau.mit

import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.UnknownHostException
import javax.swing.*

class ChatGUI {
    private val user: User

    private val frame: JFrame
    private val name        = JTextField(10)
    private var serverIP    = JTextField(9)
    private var messageText = JTextArea()
    private var chatArea    = JTextPane()

    constructor(port: Int = 0) {
        user = User("Unknown", MessengerImpl(chatArea), port)
        frame = JFrame("ReChat")
        setupFrame()

        user.start()
    }

    private fun buildToolbar(): JToolBar {
        val toolbar = JToolBar(JToolBar.HORIZONTAL).let {
            it.maximumSize = Dimension(frame.width, 20)
            it.isFloatable = false
            it.layout = FlowLayout(FlowLayout.LEFT)
            it.margin = Insets(0, 0, 0, 0)
            return@let it
        }

        JLabel("Server IP: ").let {
            it.verticalAlignment = SwingConstants.CENTER
            toolbar.add(it)
        }

        serverIP.let {
            it.text = "127.0.0.1"
            toolbar.add(it)
        }

        JButton("Connect").let {
            it.margin = Insets(3, 5, 3, 5)
            it.addActionListener {
                try {
                    serverIP.text.let {
                        val host = InetAddress.getByName(it.substring(0, it.lastIndexOf(":"))).hostName
                        val port = it.substring(it.lastIndexOf(":") + 1).toInt()
                        user.connect(InetSocketAddress(host, port))
                    }
                } catch (e: UnknownHostException) {
                    JOptionPane.showMessageDialog(frame, "Incorrect server IP!")
                } catch (e: Exception) {
                    JOptionPane.showMessageDialog(frame, "Incorrect IP format!")
                }
            }

            toolbar.add(it)
        }

        JButton("Disconnect").let {
            it.margin = Insets(3, 5, 3, 5)
            it.addActionListener {
                user.disconnect()
            }

            toolbar.add(it)
        }

        return toolbar
    }

    private fun buildNamePanel(): JToolBar {
        val toolbar = JToolBar(JToolBar.HORIZONTAL).let {
            it.maximumSize = Dimension(frame.width, 20)
            it.isFloatable = false
            it.layout = FlowLayout(FlowLayout.LEFT)
            it.margin = Insets(0, 0, 0, 0)
            return@let it
        }

        JLabel("Your name: ").let {
            it.verticalAlignment = SwingConstants.CENTER
            toolbar.add(it)
        }

        name.let {
            it.text = "Unknown"
            it.addActionListener {
                user.setName(name.text)
            }
            toolbar.add(it)
        }


        return toolbar
    }

    private fun buildChatArea(): JScrollPane {
        chatArea.let {
            it.contentType = "text/html"
            it.isEditable = false
        }

        JScrollPane(chatArea).let {
            it.setSize(frame.width, 100)
            return it
        }
    }

    private fun buildMessageArea(): JPanel {
        val panel = JPanel().let {
            it.layout = BoxLayout(it, BoxLayout.X_AXIS)
            it.maximumSize = Dimension(frame.width, 20)
            return@let it
        }

        panel.add(JScrollPane(messageText))

        JButton("Send").let {
            it.margin = Insets(10, 10, 10, 10)
            it.addActionListener {
                user.sendMessage(messageText.text)
                messageText.text = ""
            }
            panel.add(it)
        }

        return panel
    }

    private fun setupFrame() {
        frame.let {
            it.addWindowListener(object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent?) {
                    user.stop()
                    super.windowClosing(e)
                }
            })

            it.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            it.setSize(350, 500)
            it.isResizable = false
            it.layout = BorderLayout()
        }

        JPanel().let {
            it.layout = BoxLayout(it, BoxLayout.Y_AXIS)
            it.add(buildToolbar())
            it.add(buildNamePanel())
            it.add(buildChatArea())
            it.add(buildMessageArea())
            frame.add(it, BorderLayout.CENTER)
        }
    }

    fun show() {
        frame.isVisible = true
    }
}

fun main(args: Array<String>) {
    ChatGUI().show()
}