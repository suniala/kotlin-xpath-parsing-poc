package xpp

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory


typealias Builder<T> = XPathGetters.() -> T

interface XPathGetters {
    fun <T> list(expression: String, builder: Builder<T>): List<T>
    fun int(expression: String): Int
    fun string(expression: String): String
}

class XPathDocGetters(private val doc: Node) : XPathGetters {
    override fun <T> list(expression: String, builder: Builder<T>): List<T> {
        val compiledExpression = xPathExpression(expression)
        val nodes = compiledExpression.evaluate(doc, XPathConstants.NODESET) as NodeList
        return (0 until nodes.length).map {
            val item = nodes.item(it)
            XPathDocGetters(item).builder()
        }
    }

    override fun int(expression: String): Int {
        return nonEmptyString(expression).toInt()
    }

    override fun string(expression: String): String {
        return nonEmptyString(expression)
    }

    private fun nonEmptyString(expression: String): String {
        val compiledExpression = xPathExpression(expression)
        val string = compiledExpression.evaluate(doc, XPathConstants.STRING) as String
        require(string.isNotEmpty()) {
            """"$expression" must be non-empty"""
        }
        return string
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
        val docBuilder = factory.newDocumentBuilder()
        val doc = docBuilder.parse(xml)

        return XPathDocGetters(doc).builder()
    }
}