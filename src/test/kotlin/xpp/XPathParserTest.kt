package xpp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XPathParserTest {
    data class Person(val name: String, val age: Int)

    @Test
    fun simpleBuilder() {
        val builder: Builder<Person> = { x ->
            Person(
                name = x.string("/mydoc/name/text()"),
                age = x.int("/mydoc/age/text()")
            )
        }
        val parser = XPathParser(builder)

        val res = parser.parse(
            """
                        <mydoc>
                            <name>Teppo</name>
                            <age>42</age>
                        </mydoc>
                    """.trimIndent().byteInputStream(Charsets.UTF_8)
        )
        assertEquals(Person("Teppo", 42), res)

    }
}