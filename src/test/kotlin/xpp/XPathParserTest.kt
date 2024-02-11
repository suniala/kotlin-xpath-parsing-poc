package xpp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XPathParserTest {
    data class Person(val name: String, val age: Int, val relatives: List<Relative>)
    data class Relative(val name: String, val age: Int)

    @Test
    fun nestedBuilder() {
        val builder: Builder<Person> = {
            Person(
                name = string("/mydoc/name/text()"),
                age = int("/mydoc/age/text()"),
                relatives = list("/mydoc/relatives/relative") {
                    Relative(
                        name = string("name/text()"),
                        age = int("age/text()")
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