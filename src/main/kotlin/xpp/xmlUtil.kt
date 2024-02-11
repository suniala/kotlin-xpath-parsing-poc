package xpp

import org.w3c.dom.Node
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Suppress("unused")
fun nodeToString(node: Node): String {
    val sw = StringWriter()
    try {
        val t: Transformer = TransformerFactory.newInstance().newTransformer()
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        t.setOutputProperty(OutputKeys.INDENT, "yes")
        t.transform(DOMSource(node), StreamResult(sw))
    } catch (e: TransformerException) {
        throw RuntimeException(e)
    }
    return sw.toString()
}
