package ru.spbau.mit

import ru.spbau.mit.messenger.Messenger
import ru.spbau.mit.messenger.MessengerImpl
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.awt.*
import java.awt.event.*
import java.net.InetAddress
import java.net.UnknownHostException
import javax.swing.*
import javax.swing.border.BevelBorder
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class ChatGUI {
    private val user: User
    private val messenger: Messenger

    private val frame: JFrame
    private val name: JTextField = JTextField(10)
    private var serverIP: JTextField = JTextField(9)
    private var messageText: JTextArea = JTextArea()
    private var chatArea: JTextPane = JTextPane()

    private val nameStyle: SimpleAttributeSet = SimpleAttributeSet()
    private val textStyle: SimpleAttributeSet = SimpleAttributeSet()
    private val infoStyle: SimpleAttributeSet = SimpleAttributeSet()

    private var lastTimeKeyPressed: Long = 0
    private var status: JLabel = JLabel()
    private val timer: Timer

    constructor(port: Int = 0) {
        messenger = MessengerImpl()
        messenger.setOnMessageReceived { onMessageReceived(it) }
        messenger.setOnSystemMessageReceived { onSystemMessageReceived(it) }
        messenger.setOnMessageSent { onMessageSent(it) }

        user = User("Unknown", messenger, port)

        frame = JFrame("ReChat")
        setupFrame()
        setupFontStyle()

        timer = Timer(100) {
            if (System.currentTimeMillis() - lastTimeKeyPressed < 1000) {
                messenger.sendTypingStatus(true)
            } else {
                messenger.sendTypingStatus(false)
            }
        }

        timer.start()
        user.start()
    }

    fun show() {
        frame.isVisible = true
    }

    /**
     * Callbacks that display messages to GUI
     */

    private fun onMessageReceived(message: Message) {
        EventQueue.invokeLater {
            when (message.bodyCase) {
                Message.BodyCase.TEXTMESSAGE -> {
                    StyleConstants.setForeground(nameStyle, Color(30, 54, 191))

                    chatArea.styledDocument.let {
                        it.insertString(it.length, "${message.textMessage.owner}\n", nameStyle)
                        it.insertString(it.length, "${message.textMessage.text}\n", textStyle)
                    }
                }
                Message.BodyCase.TYPINGSTATUS -> {
                    if (message.typingStatus.typing) {
                        status.text = "User typing..."
                    } else {
                        status.text = ""
                    }
                }
                else -> throw NotImplementedException()
            }
        }
    }

    private fun onSystemMessageReceived(message: String) {
        EventQueue.invokeLater {
            chatArea.styledDocument.let {
                it.insertString(it.length, "$message\n", infoStyle)
            }
        }
    }

    private fun onMessageSent(message: Message) {
        EventQueue.invokeLater {
            when (message.bodyCase) {
                Message.BodyCase.TEXTMESSAGE -> {
                    StyleConstants.setForeground(nameStyle, Color(30, 191, 43))

                    chatArea.styledDocument.let {
                        it.insertString(it.length, "${message.textMessage.owner}\n", nameStyle)
                        it.insertString(it.length, "${message.textMessage.text}\n", textStyle)
                    }
                }
                Message.BodyCase.TYPINGSTATUS -> { /* ignore */
                }
                else -> throw NotImplementedException()
            }
        }
    }

    /**
     * Methods to build GUI
     */

    private fun setupFontStyle() {
        StyleConstants.setFontFamily(nameStyle, "Trebuchet MS")
        StyleConstants.setBold(nameStyle, true)
        StyleConstants.setFontSize(nameStyle, 12)

        StyleConstants.setFontFamily(textStyle, "Trebuchet MS")
        StyleConstants.setForeground(textStyle, Color.BLACK)
        StyleConstants.setFontSize(textStyle, 12)

        StyleConstants.setFontFamily(infoStyle, "Trebuchet MS")
        StyleConstants.setItalic(infoStyle, true)
        StyleConstants.setForeground(infoStyle, Color.DARK_GRAY)
        StyleConstants.setFontSize(infoStyle, 11)
        StyleConstants.setAlignment(infoStyle, StyleConstants.ALIGN_CENTER)
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
                        user.connect(host, port)
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

    private fun buildStatusBar(): JPanel {
        val panel = JPanel().let {
            it.border = BevelBorder(BevelBorder.LOWERED)
            it.preferredSize = Dimension(frame.width, 20)
            it.layout = BoxLayout(it, BoxLayout.X_AXIS)
            return@let it
        }

        status.let {
            it.horizontalAlignment = SwingConstants.LEFT
            panel.add(it)
        }

        return panel
    }

    private fun buildMessageArea(): JPanel {
        val panel = JPanel().let {
            it.layout = BoxLayout(it, BoxLayout.X_AXIS)
            it.maximumSize = Dimension(frame.width, 20)
            return@let it
        }

        messageText.addKeyListener(object : KeyListener {
            override fun keyPressed(e: KeyEvent?) {
                lastTimeKeyPressed = System.currentTimeMillis()
            }

            override fun keyReleased(e: KeyEvent?) {
                lastTimeKeyPressed = System.currentTimeMillis()
            }

            override fun keyTyped(e: KeyEvent?) {
                lastTimeKeyPressed = System.currentTimeMillis()
            }
        })

        JScrollPane(messageText).let {
            panel.add(it)
        }


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
                    timer.stop()
                    user.stop()
                    super.windowClosing(e)
                }
            })

            it.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            it.setSize(360, 500)
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
            frame.add(buildStatusBar(), BorderLayout.SOUTH)
        }
    }
}