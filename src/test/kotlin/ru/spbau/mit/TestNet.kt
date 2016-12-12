package ru.spbau.mit

import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.spbau.mit.messenger.MessengerImpl
import ru.spbau.mit.net.ChatClient
import ru.spbau.mit.net.ClientImpl
import ru.spbau.mit.net.ChatServer
import ru.spbau.mit.net.ServerImpl
import kotlin.test.assertEquals

class TestNet {
    private val DELAY: Long = 1000

    private val messenger1 = MessengerImpl()
    private val messages1 = mutableListOf<Message>()
    private val systemMessages1 = mutableListOf<String>()
    private lateinit var server: ChatServer

    private val messenger2 = MessengerImpl()
    private val messages2 = mutableListOf<Message>()
    private val systemMessages2 = mutableListOf<String>()
    private lateinit var client: ChatClient

    init {
        messenger1.setOnSystemMessageReceived { message ->
            systemMessages1.add(message)
        }
        messenger1.setOnMessageReceived { message ->
            if (message.bodyCase == Message.BodyCase.TEXTMESSAGE) {
                messages1.add(message)
            }
        }

        messenger2.setOnSystemMessageReceived { message ->
            systemMessages2.add(message)
        }
        messenger2.setOnMessageReceived { message ->
            if (message.bodyCase == Message.BodyCase.TEXTMESSAGE) {
                messages2.add(message)
            }
        }
    }

    @Before
    fun setupUsers() {
        server = ServerImpl(2001)
        server.setOnConnected { messenger1.sendSystemMessage("User connected!") }
        server.setOnDisconnected { messenger1.sendSystemMessage("User disconnected!") }
        server.setOnStreamObserverUpdated { messenger1.setStream(it) }
        server.setOnMessageReceived { messenger1.receiveMessage(it) }

        client = ClientImpl()
        client.setOnConnected {
            messenger2.sendTypingStatus(false)
            messenger2.sendSystemMessage("You connected!")
        }
        client.setOnConnectionFailed { messenger2.sendSystemMessage("Failed to connect!") }
        client.setOnDisconnected { messenger2.sendSystemMessage("You disconnected!") }
        client.setOnStreamObserverUpdated { messenger2.setStream(it) }
        client.setOnMessageReceived { messenger2.receiveMessage(it) }
    }

    @After
    fun clearMessageHistory() {
        messages1.clear()
        systemMessages1.clear()

        messages2.clear()
        systemMessages2.clear()

        server.shutdown()
    }

    @Test
    fun testConnection() {
        server.start()
        client.connect("127.0.0.1", 2001)
        Thread.sleep(DELAY)

        server.stop()
        client.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages2.toList())
    }

    @Test
    fun testReconnection() {
        server.start()
        client.connect("127.0.0.1", 2001)
        Thread.sleep(DELAY)

        client.disconnect()
        Thread.sleep(DELAY)
        client.connect("127.0.0.1", 2001)
        Thread.sleep(DELAY)

        server.stop()
        Thread.sleep(DELAY)
        client.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!", "User connected!", "User disconnected!"),
                systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!", "You connected!", "You disconnected!"),
                systemMessages2.toList())
    }

    @Test
    fun testMessageSending() {
        server.start()
        client.connect("127.0.0.1", 2001)
        Thread.sleep(DELAY)

        messenger2.sendMessage("ChatClient", "Hi, Server!")
        messenger1.sendMessage("Server", "Hi, ChatClient!")
        Thread.sleep(DELAY)

        server.stop()
        Thread.sleep(DELAY)
        client.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages2.toList())

        assertEquals(listOf(buildMessage("ChatClient", "Hi, Server!")), messages1.toList())
        assertEquals(listOf(buildMessage("Server", "Hi, ChatClient!")), messages2.toList())
    }

    fun buildMessage(owner: String, text: String): Message {
        val textMessage = TextMessage.newBuilder().setOwner(owner).setText(text).build()
        return Message.newBuilder().setTextMessage(textMessage).build()
    }
}