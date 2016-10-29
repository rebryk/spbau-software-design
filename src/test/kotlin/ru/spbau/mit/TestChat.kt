package ru.spbau.mit

import org.junit.Test

class TestChat {

    @Test
    fun testServer() {
        val user = User("User", MessengerImpl(null))
        user.start()
        user.stop()
    }
}
