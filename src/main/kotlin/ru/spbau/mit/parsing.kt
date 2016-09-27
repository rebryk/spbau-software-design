package ru.spbau.mit

import java.util.*

/**
 * Created by rebryk on 9/10/16.
 */

/**
 * Method skip characters until current character != first character
 */
fun skipString(string: String, start: Int): Int {
    var end = start + 1
    while (end < string.length && string[end] != string[start]) {
        ++end
    }

    return end
}

/**
 * Method makes substitution
 */
fun substitute(input: String, variables: MutableMap<String, String>): String {
    return input.split(' ')
            .map { if (it.isNotEmpty() && it.first() == '$') variables.getOrElse(it, { "" }) else it }
            .joinToString(" ")
}

/**
 * Method makes arguments substitution
 */
fun processStrings(input: String, variables: MutableMap<String, String>): String {
    val builder = StringBuilder()

    var l = 0
    var r = 0
    while (r < input.length) {
        if (input[r] == '\'' || input[r] == '\"') {
            builder.append(substitute(input.substring(l, r), variables))
            l = r
            r = skipString(input, r)

            if (input[r] == '\'') {
                builder.append(input.substring(l, r + 1))
            } else {
                builder.append(input[l]).append(substitute(input.substring(l + 1, r), variables)).append(input[r])
            }

            l = r + 1
        }

        ++r
    }

    if (l < r) {
        builder.append(substitute(input.substring(l, r), variables))
    }

    return builder.toString()
}

/**
 * Methods split the string by the given character ()
 */
fun splitBy(input: String, ch: Char): ArrayList<String> {
    val result = ArrayList<String>()
    var i = 0
    var l = 0

    while (i < input.length) {
        if (input[i].compareTo(ch) == 0) {
            result.add(input.substring(l, i))
            l = i + 1
        } else {
            if (input[i].compareTo('\'') == 0 || input[i].compareTo('\"') == 0) {
                i = skipString(input, i)
            }
        }
        ++i
    }

    if (l < input.length) {
        result.add(input.substring(l, input.length))
    }

    return result
}

/**
 * Method checks that given char is quote
 */
fun isQuote(ch: Char): Boolean {
    return ch.compareTo('\'') == 0 || ch.compareTo('\"') == 0
}

/**
 * Methods removes the first and the last character if they are quotes
 */
fun unwrap(input: String): String {
    if (input.length > 1 && isQuote(input.first()) && input.first().compareTo(input.last()) == 0) {
        return input.substring(1, input.length - 1)
    }

    return input
}
