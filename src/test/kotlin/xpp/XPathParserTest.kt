package xpp

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class XPathParserTest {
    @Test
    fun simpleParse() {
        val p = XPathParser()
        val res = p.parseString(
            """
                        <mydoc>
                            <some>Asdf</some>
                        </mydoc>
                    """.trimIndent().byteInputStream(Charsets.UTF_8),
            "/mydoc/some/text()"
        )
        assertEquals("Asdf", res)
    }
}