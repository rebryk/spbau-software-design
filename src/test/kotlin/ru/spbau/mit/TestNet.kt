package ru.spbau.mit

import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.spbau.mit.messenger.Message
import ru.spbau.mit.messenger.MessengerImpl
import ru.spbau.mit.net.Client
import ru.spbau.mit.net.ClientImpl
import ru.spbau.mit.net.Server
import ru.spbau.mit.net.ServerImpl
import java.net.InetSocketAddress
import kotlin.test.assertEquals

class TestNet {
    private val DELAY: Long = 200

    private val messenger1 = MessengerImpl()
    private val messages1 = mutableListOf<Message>()
    private val systemMessages1 = mutableListOf<String>()
    private var server: Server? = null

    private val messenger2 = MessengerImpl()
    private val messages2 = mutableListOf<Message>()
    private val systemMessages2 = mutableListOf<String>()
    private var client: Client? = null

    init {
        messenger1.setOnSystemMessageReceived { message ->
            systemMessages1.add(message)
        }
        messenger1.setOnMessageReceived { message ->
            messages1.add(message)
        }

        messenger2.setOnSystemMessageReceived { message ->
            systemMessages2.add(message)
        }
        messenger2.setOnMessageReceived { message ->
            messages2.add(message)
        }
    }

    @Before
    fun setupUsers() {
        server = ServerImpl(messenger1, 2001)
        server!!.setOnConnected { messenger1.sendSystemMessage("User connected!") }
        server!!.setOnDisconnected { messenger1.sendSystemMessage("User disconnected!") }

        client = ClientImpl(messenger2)
        client!!.setOnConnected { messenger2.sendSystemMessage("You connected!") }
        client!!.setOnConnectionFailed { messenger2.sendSystemMessage("Failed to connect!") }
        client!!.setOnDisconnected { messenger2.sendSystemMessage("You disconnected!") }
    }

    @After
    fun clearMessageHistory() {
        messages1.clear()
        systemMessages1.clear()

        messages2.clear()
        systemMessages2.clear()
    }

    @Test
    fun testConnection() {
        server!!.start()
        client!!.connect(InetSocketAddress("127.0.0.1", 2001))
        client!!.start()
        Thread.sleep(DELAY)

        server!!.stop()
        server!!.shutdown()
        client!!.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages2.toList())
    }

    @Test
    fun testReconnection() {
        server!!.start()
        client!!.connect(InetSocketAddress("127.0.0.1", 2001))
        client!!.start()
        Thread.sleep(DELAY)

        client!!.disconnect()
        Thread.sleep(DELAY)
        client!!.connect(InetSocketAddress("127.0.0.1", 2001))
        client!!.start()
        Thread.sleep(DELAY)

        server!!.stop()
        server!!.shutdown()
        Thread.sleep(DELAY)
        client!!.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!", "User connected!", "User disconnected!"),
                systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!", "You connected!", "You disconnected!"),
                systemMessages2.toList())
    }

    @Test
    fun testMessageSending() {
        server!!.start()
        client!!.connect(InetSocketAddress("127.0.0.1", 2001))
        client!!.start()
        Thread.sleep(DELAY)

        client!!.sendMessage(Message("Client", "Hi, Server!"))
        server!!.getClient()!!.sendMessage(Message("Server", "Hi, Client!"))
        Thread.sleep(DELAY)

        server!!.stop()
        server!!.shutdown()
        Thread.sleep(DELAY)
        client!!.disconnect()
        Thread.sleep(DELAY)

        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages1.toList())
        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages2.toList())

        assertEquals(listOf(Message("Client", "Hi, Server!")), messages1.toList())
        assertEquals(listOf(Message("Server", "Hi, Client!")), messages2.toList())
    }
}