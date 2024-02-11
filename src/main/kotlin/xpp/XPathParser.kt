package xpp

import org.w3c.dom.Document
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

typealias Builder<T> = (XPathGetters) -> T

interface XPathGetters {
    fun int(expression: String): Int
    fun string(expression: String): String
}

class XPathDocGetters(private val doc: Document) : XPathGetters {
    override fun int(expression: String): Int {
        val compiledExpression = xPathExpression(expression)
        return compiledExpression.evaluate(doc, XPathConstants.STRING).toString().toInt()
    }

    override fun string(expression: String): String {
        val compiledExpression = xPathExpression(expression)
        return compiledExpression.evaluate(doc, XPathConstants.STRING).toString()
    }

    private companion object {
        private val xpathFactory = XPathFactory.newInstance()
        private fun xPathExpression(expression: String): XPathExpression {
            val xpath = xpathFactory.newXPath()
            return xpath.compile(expression)
        }
    }
}

class XPathParser<T>(private val builder: Builder<T>) {
    fun parse(xml: ByteArrayInputStream): T {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(xml)

        val g = XPathDocGetters(doc)
        return builder(g)
    }
}