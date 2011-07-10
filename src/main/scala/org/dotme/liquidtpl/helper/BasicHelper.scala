/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.helper

import dispatch.json.JsArray
import dispatch.json.JsObject
import dispatch.json.JsString
import dispatch.json.JsValue
import javax.servlet.http.HttpServletResponse
import org.dotme.liquidtpl.Constants
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.ListBuffer
import scala.xml._
import sjson.json.Format
import sjson.json.JsonSerialization._
import sjson.json.DefaultProtocol._

object BasicHelper {
  case class EscapedPCData(_data: String) extends Atom[String](_data) {
    if (null == data) {
      throw new IllegalArgumentException("tried to construct EscapedPCData with null")
    }

    override def buildString(sb: StringBuilder) = {
      sb.append(Constants.LINE_SEPARATOR)
        .append("//<![CDATA[")
        .append(Constants.LINE_SEPARATOR)
        .append("%s".format(data))
        .append(Constants.LINE_SEPARATOR)
        .append("//]]>")
        .append(Constants.LINE_SEPARATOR)
    }
  }

  def jsonFromIntStringPairs(list: List[(Int, String)]): JsValue =
    {
      val rs: List[JsValue] = list.map { i =>
        JsObject(List(
          (JsString(Constants.KEY_KEY) -> tojson(i._1)),
          (JsString(Constants.KEY_VALUE) -> tojson(i._2))))
      }
      JsArray(rs)
    }

  def jsonFromStringPairs(list: List[(String, String)]): JsValue =
    {
      val rs: List[JsValue] = list.map { i =>
        JsObject(List(
          (JsString(Constants.KEY_KEY) -> tojson(i._1)),
          (JsString(Constants.KEY_VALUE) -> tojson(i._2))))
      }
      JsArray(rs)
    }

  def JsonTag(name: String, data: JsValue): Node = {
    val buf: StringBuilder = new StringBuilder
    buf.append("var ")
      .append(name)
      .append(" = ")
      .append(jsValueOutPut(data))
      .append(";")
    <script type="text/javascript">{ EscapedPCData(buf.toString) }</script>
  }

  def writeJsonCommentFiltered(response: HttpServletResponse, data: JsValue) = {
    response.resetBuffer();
    response.setHeader(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_TEXT);
    response.setCharacterEncoding(Constants.CHARSET)
    response.getWriter.print("/*%s*/".format(jsValueOutPut(data)))
    response.flushBuffer();
  }

  def writeJsonRaw(response: HttpServletResponse, data: JsValue) = {
    response.resetBuffer();
    response.setHeader(Constants.HEADER_CONTENT_TYPE, Constants.HEADER_CONTENT_TYPE_JSON);
    response.setCharacterEncoding(Constants.CHARSET)
    response.getWriter.print("%s".format(jsValueOutPut(data)))
    response.flushBuffer();
  }

  def jsValueOutPut(data: JsValue): String = {
    data.toString.replaceAll("\r", "")
      .replaceAll("\r\n", "\n")
      .replaceAll("[\r\n]", "\\\\n")
      .replaceAll("[\t]", "\\\\t")
  }

  def attribute(kvs: (String, String)*) = new {
    def +:(elem: xml.Elem) = elem % (xml.Node.NoAttributes /: kvs) {
      case (attr, (k, v)) => new xml.UnprefixedAttribute(k, v, attr)
    }
  }

  def textToHtml(text: String): NodeSeq = {
    text.replaceAll("\r\n", "\n").split("\r|\n|\r\n").flatMap { line =>
      <p>{ autoLink(line) }</p>
    }.toSeq
  }

  def autoLink(text: String): NodeSeq = {
    val URLPATTERN = "((http|https|ftp):\\/\\/[\\w?=&.\\/-;#~%-]+(?![\\w\\s?&.\\/;#~%\"=-]*>))".r
    URLPATTERN.findFirstMatchIn(text) match {
      case None => Text(text)
      case Some(m) =>
        <xml:group>{ Text(m.before.toString) }<a target="_blank" class="ui-link" href={ m.matched.toString }>{ m.matched.toString }</a>{ autoLink(m.after.toString) }</xml:group>
    }
  }
}