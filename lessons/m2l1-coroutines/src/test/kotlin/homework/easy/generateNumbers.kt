package ru.otus.otuskotlin.coroutines.homework.easy

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

suspend fun generateNumbers() : List<Int> {
    val scope = CoroutineScope(Dispatchers.IO)
    val result = scope.async {
        (0..10000).map {
            (0..100).random()
        }
    }
    return result.await()
}
