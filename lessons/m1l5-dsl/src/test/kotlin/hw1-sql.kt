import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class Hw1Sql {
    private fun checkSQL(expected: String, sql: SqlSelectBuilder) {
        assertEquals(expected, sql.build())
    }

    @Test
    fun `simple select all from table`() {
        val expected = "select * from table"

        val real = query {
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `check that select can't be used without table`() {
        assertFailsWith<Exception> {
            query {
                select("col_a")
            }.build()
        }
    }

    @Test
    fun `select certain columns from table`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    @Test
    fun `select certain columns from table 1`() {
        val expected = "select col_a, col_b from table"

        val real = query {
            select("col_a", "col_b")
            from("table")
        }

        checkSQL(expected, real)
    }

    /**
     * __eq__ is "equals" function. Must be one of char:
     *  - for strings - "="
     *  - for numbers - "="
     *  - for null - "is"
     */
    @Test
    fun `select with complex where condition with one condition`() {
        val expected = "select * from table where col_a = 'id'"

        val real = query {
            from("table")
            where { "col_a" eq "id" }
        }

        checkSQL(expected, real)
    }

    /**
     * __nonEq__ is "non equals" function. Must be one of chars:
     *  - for strings - "!="
     *  - for numbers - "!="
     *  - for null - "!is"
     */
    @Test
    fun `select with complex where condition with two conditions`() {
        val expected = "select * from table where col_a != 0"

        val real = query {
            from("table")
            where {
                "col_a" nonEq 0
            }
        }

        checkSQL(expected, real)
    }

    @Test
    fun `when 'or' conditions are specified then they are respected`() {
        val expected = "select * from table where (col_a = 4 or col_b !is null)"

        val real = query {
            from("table")
            where {
                or {
                    "col_a" eq 4
                    "col_b" nonEq null
                }
            }
        }

        checkSQL(expected, real)
    }
}

class SqlSelectBuilder {
    private val parts: MutableList<String> = mutableListOf()

    fun select(vararg columns: String) {
        parts.add("select ${columns.joinToString(", ")}")
    }

    fun from(table: String) {
        when {
            parts.joinToString().contains("select") -> parts.add("from $table")
            else -> parts.add("select * from $table")
        }
    }

    fun where(expression: SqlExpressionBuilder.() -> Unit) {
        val builder = SqlExpressionBuilder()
        builder.expression()
        parts.add("where ${builder.build()}")
    }

    fun build(): String {
        val result = parts.joinToString(" ")
        when {
            result.contains("from") -> return result
            else -> throw Exception()
        }
    }

    inner class SqlExpressionBuilder {
        private val conditions: MutableList<String> = mutableListOf()

        infix fun String.eq(value: Any) {
            conditions.add("$this = ${formatValue(value)}")
        }

        infix fun String.nonEq(value: Any?) {
            when (value){
               null -> conditions.add("$this !is ${value?.let { formatValue(it) }}")
               else -> conditions.add("$this != ${value?.let { formatValue(it) }}")
            }
        }

        infix fun or(expression: SqlExpressionBuilder.() -> Unit) {
            val builder = SqlExpressionBuilder()
            builder.expression()
            conditions.add("(${builder.build()})")
        }

        fun build(): String {
            return conditions.joinToString(" or ")
        }

        private fun formatValue(value: Any): String {
            return when (value) {
                is String -> "'$value'"
                null -> "null"
                else -> value.toString()
            }
        }
    }
}
        fun query(builder: SqlSelectBuilder.() -> Unit): SqlSelectBuilder {
            return SqlSelectBuilder().apply(builder)
        }
