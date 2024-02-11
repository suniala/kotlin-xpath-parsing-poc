package xpp

import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class XPathParser {
    fun parseString(xml: ByteArrayInputStream, expression: String): String {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val doc = builder.parse(xml)

        val xpathFactory = XPathFactory.newInstance()
        val xpath = xpathFactory.newXPath()
        val compiledExpression = xpath.compile(expression)

        return compiledExpression.evaluate(doc, XPathConstants.STRING) as String
    }
}