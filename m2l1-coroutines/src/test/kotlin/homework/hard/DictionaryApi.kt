package ru.otus.otuskotlin.coroutines.homework.hard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.Response
import ru.otus.otuskotlin.coroutines.homework.hard.dto.Dictionary

class DictionaryApi(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

    suspend fun findWord(locale: Locale, word: String): Dictionary? { // make something with context
        val url = "$DICTIONARY_API/${locale.code}/$word"
        println("Searching $url")
        val scope = CoroutineScope(Dispatchers.IO)
        val result = scope.async {
            getBody(HttpClient.get(url).execute())?.firstOrNull()
        }
        return result.await()
    }


    private suspend fun getBody(response: Response): List<Dictionary>? {
        if (!response.isSuccessful) {
            return emptyList()
        }

        return response.body?.let {
            objectMapper.readValue(it.string())
        }
    }
}
