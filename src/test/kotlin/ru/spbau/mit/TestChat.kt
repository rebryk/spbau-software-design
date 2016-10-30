package ru.spbau.mit

import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.spbau.mit.messenger.Message
import ru.spbau.mit.messenger.MessengerImpl
import java.net.InetSocketAddress
import kotlin.test.assertEquals

class TestChat {
    private val DELAY: Long = 200

    private val messenger1 = MessengerImpl()
    private val messages1 = mutableListOf<Message>()
    private val systemMessages1 = mutableListOf<String>()
    private var user1: User? = null

    private val messenger2 = MessengerImpl()
    private val messages2 = mutableListOf<Message>()
    private val systemMessages2 = mutableListOf<String>()
    private var user2: User? = null

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
        user1 = User("User1", messenger1, 2001)
        user2 = User("User2", messenger2, 2002)
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
        user1!!.start()
        user2!!.start()

        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)

        user1!!.stop()
        user2!!.stop()
        Thread.sleep(DELAY)

        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages1.toList())
        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages2.toList())
    }

    @Test
    fun testReconnection() {
        user1!!.start()
        user2!!.start()

        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)
        user1!!.disconnect()
        Thread.sleep(DELAY)
        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)

        user1!!.stop()
        user2!!.stop()
        Thread.sleep(DELAY)

        assertEquals(listOf("You connected!", "You disconnected!", "You connected!", "You disconnected!"),
                systemMessages1.toList())
        assertEquals(listOf("User connected!", "User disconnected!", "User connected!", "User disconnected!"),
                systemMessages2.toList())
    }

    @Test
    fun testCrossReconnection() {
        user1!!.start()
        user2!!.start()

        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)
        user2!!.connect(InetSocketAddress("127.0.0.1", 2001))
        Thread.sleep(DELAY)

        user1!!.stop()
        user2!!.stop()
        Thread.sleep(DELAY)

        assertEquals(listOf("You connected!", "You disconnected!", "User connected!", "User disconnected!"),
                systemMessages1.toList())
        assertEquals(listOf("User connected!", "User disconnected!", "You connected!", "You disconnected!"),
                systemMessages2.toList())
    }

    @Test
    fun testMessageSending() {
        user1!!.start()
        user2!!.start()

        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)

        user1!!.sendMessage("Hi, User2!")
        user2!!.sendMessage("Hi, User1!")
        Thread.sleep(DELAY)

        user1!!.stop()
        user2!!.stop()
        Thread.sleep(DELAY)

        assertEquals(listOf("You connected!", "You disconnected!"), systemMessages1.toList())
        assertEquals(listOf("User connected!", "User disconnected!"), systemMessages2.toList())

        assertEquals(listOf(Message("User2", "Hi, User1!")), messages1.toList())
        assertEquals(listOf(Message("User1", "Hi, User2!")), messages2.toList())
    }

    @Test
    fun testNameChanging() {
        user1!!.start()
        user2!!.start()

        user1!!.connect(InetSocketAddress("127.0.0.1", 2002))
        Thread.sleep(DELAY)

        user1!!.sendMessage("Hi, User2!")
        user2!!.sendMessage("Hi, User1!")
        Thread.sleep(DELAY)

        user1!!.setName("NewUser1")
        user2!!.setName("NewUser2")

        user1!!.sendMessage("Hi, NewUser2!")
        user2!!.sendMessage("Hi, NewUser1!")
        Thread.sleep(DELAY)

        user1!!.stop()
        user2!!.stop()
        Thread.sleep(DELAY)

        assertEquals(listOf("You connected!", "Your name has changed to NewUser1", "You disconnected!"),
                systemMessages1.toList())
        assertEquals(listOf("User connected!", "Your name has changed to NewUser2", "User disconnected!"),
                systemMessages2.toList())

        assertEquals(listOf(Message("User2", "Hi, User1!"), Message("NewUser2", "Hi, NewUser1!")), messages1.toList())
        assertEquals(listOf(Message("User1", "Hi, User2!"), Message("NewUser1", "Hi, NewUser2!")), messages2.toList())
    }
}
