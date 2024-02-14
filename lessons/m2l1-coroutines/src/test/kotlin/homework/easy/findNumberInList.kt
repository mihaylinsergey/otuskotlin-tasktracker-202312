package homework.easy

import kotlinx.coroutines.*

suspend fun findNumberInList(toFind: Int, numbers: List<Int>): Int {
    val scope = CoroutineScope(Dispatchers.IO)
    val result = scope.async {
        delay(2000L)
        numbers.firstOrNull { it == toFind } ?: -1
    }
    return result.await()
}
