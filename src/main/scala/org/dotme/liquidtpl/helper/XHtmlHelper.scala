/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.helper

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.Writer
import org.dotme.liquidtpl.Constants
import scala.io.Source
import scala.xml.NodeSeq
import scala.xml.XML
import scala.xml.dtd.DocType
import scala.xml.dtd.PublicID
import scala.xml.parsing.XhtmlParser
import java.io.StringWriter

object XhtmlHelper {

  val DOCTYPE_PUBLIC_ID: String = "-//W3C//DTD XHTML 1.0 Transitional//EN"
  val EXTERNAL_DTD_PATH: String = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
  val DOCTYPE: DocType = DocType("html",
    new PublicID(DOCTYPE_PUBLIC_ID, EXTERNAL_DTD_PATH),
    Seq())

  @throws(classOf[Exception])
  def load(file: String, wrapper: Option[String]): NodeSeq = {
    var xml: NodeSeq = null;
    val stream = new FileInputStream(new File(file))
    val reader: BufferedReader = new BufferedReader(new InputStreamReader(stream, Constants.CHARSET))
    try {
      val buf: StringBuilder = new StringBuilder
      var line = "";
      while ({ line = reader.readLine; line != null }) {
        buf.append(line)
        buf.append(Constants.LINE_SEPARATOR)
      }
      wrapper match {
        case Some(v) =>
          buf.insert(0, "<%s>".format(v)).append("</%s>".format(v))
        case None =>
      }

      var string = buf.toString.replaceAll("></", "> </")
      val src = Source.fromString(string)
      xml = XhtmlParser(src).theSeq.asInstanceOf[NodeSeq]
      src.close
    } finally {
      reader.close
    }
    xml
  }

  @throws(classOf[Exception])
  def load(file: String): NodeSeq = {
    load(file, None)
  }

  @throws(classOf[Exception])
  def load(file: String, wrapper: String): NodeSeq = {
    load(file, Some(wrapper))
  }

  def write(writer:Writer, xml:NodeSeq):Unit = {
    xml.foreach{ n =>
      XML.write(writer, n, Constants.CHARSET, false, DOCTYPE)
    }
  }
}