package xpp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XPathParserTest {
    data class Person(val name: String, val age: Int, val relatives: List<Relative>)
    data class Relative(val name: String, val age: Int)

    @Test
    fun simpleBuilder() {
        val builder: Builder<Person> = { x ->
            Person(
                name = x.string("/mydoc/name/text()"),
                age = x.int("/mydoc/age/text()"),
                relatives = x.list("/mydoc/relatives/relative") { x2 ->
                    Relative(
                        name = x2.string("name/text()"),
                        age = x2.int("age/text()")
                    )
                }
            )
        }
        val parser = XPathParser(builder)

        val res = parser.parse(
            """
                        <mydoc>
                            <name>Teppo</name>
                            <age>42</age>
                            <relatives>
                              <relative><name>Matti</name><age>32</age></relative>
                              <relative><name>Seppo</name><age>62</age></relative>
                            </relatives>
                        </mydoc>
                    """.trimIndent().byteInputStream(Charsets.UTF_8)
        )
        assertEquals(
            Person(
                "Teppo",
                42,
                listOf(Relative("Matti", 32), Relative("Seppo", 62))
            ), res
        )

    }
}