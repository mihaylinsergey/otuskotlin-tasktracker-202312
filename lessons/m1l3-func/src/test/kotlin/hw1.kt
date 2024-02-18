import kotlin.test.Test
import kotlin.test.assertEquals

/*
* Реализовать функцию, которая преобразует список словарей строк в ФИО
* Функцию сделать с использованием разных функций для разного числа составляющих имени
* Итого, должно получиться 4 функции
*
* Для успешного решения задания, требуется раскомментировать тест, тест должен выполняться успешно
* */
class HomeWork1Test {

    @Test
    fun mapListToNamesTest() {
        val input = listOf(
            mapOf(
                "first" to "Иван",
                "middle" to "Васильевич",
                "last" to "Рюрикович",
            ),
            mapOf(
                "first" to "Петька",
            ),
            mapOf(
                "first" to "Сергей",
                "last" to "Королев",
            ),
        )
        val expected = listOf(
            "Рюрикович Иван Васильевич",
            "Петька",
            "Королев Сергей",
        )
        val res = mapListToNames(input)
        assertEquals(expected, res)
    }

    fun mapListToNames(list: List<Map<String, String>>): List<String> {
        val result = mutableListOf<String>()
        for (map in list) {
            val firstName = map["first"] ?: ""
            val middleName = map["middle"] ?: ""
            val lastName = map["last"] ?: ""
            val fullName = when {
                firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty() ->
                    "$lastName $firstName $middleName"
                firstName.isNotEmpty() && middleName.isEmpty() && lastName.isNotEmpty() -> "$lastName $firstName"
                firstName.isNotEmpty() && middleName.isEmpty() && lastName.isEmpty() -> firstName
                else -> ""
            }
            result.add(fullName)
        }
        return result
    }
}