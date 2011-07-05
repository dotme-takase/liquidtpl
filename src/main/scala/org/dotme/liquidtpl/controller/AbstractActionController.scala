/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller

import org.dotme.liquidtpl.LanguageUtil
import org.dotme.liquidtpl.helper.BasicHelper
import org.dotme.liquidtpl.helper.XhtmlHelper
import org.slim3.controller.Controller
import org.slim3.controller.Navigation
import scala.xml.{ Node, NodeSeq, Elem, Text }
import java.io.File
import java.util.logging.Logger
import org.dotme.liquidtpl.Constants

object AbstractActionController {
  val logger = Logger.getLogger(AbstractActionController.getClass.getName)

  def replacer(in: NodeSeq, map: Map[String, ((Node) => NodeSeq)]): NodeSeq = in match {
    case Elem(p, l, a, ns, child @ _*) =>
      if ((p == Constants.TAG_NAMESPACE) && map.keySet.contains(l)) {
        map.apply(l)(in.apply(0))
      } else {
        Elem(p, l, a, ns, child.flatMap(replacer(_, map)): _*)
      }
    case x => x
  }
}

abstract class AbstractActionController extends Controller {
  val logger = Logger.getLogger(AbstractActionController.getClass.getName)
  @throws(classOf[Exception])
  override protected def run(): Navigation = {
    try {
      response.setCharacterEncoding(Constants.CHARSET)
      val outer = getOuterTemplate
      val content = AbstractActionController.replacer(outer, contentReplacerMap)
      val xml: NodeSeq = AbstractActionController.replacer(content, replacerMap)
      val filteredXml = nodeFilter(xml);
      XhtmlHelper.write(response.getWriter, filteredXml)
    } catch {
      case e: Exception =>
        response.getWriter().println(e)
        response.getWriter().println(e.printStackTrace)
    }
    response.flushBuffer();
    return null;
  }

  protected def contentReplacerMap: Map[String, ((Node) => NodeSeq)] = {
    val _content: NodeSeq = getContent
    import sjson.json.JsonSerialization._
    import sjson.json.DefaultProtocol._
    val contentPlus: NodeSeq = <div>{
      _content ++
        Constants.LINE_SEPARATOR ++
        BasicHelper.JsonTag(Constants.JSVALUE_PAGE_INFO, tojson(getPageInfo)) ++
        Constants.LINE_SEPARATOR
    }</div>

    Map(
      "content" -> { e => contentPlus })
  }

  protected def replacerMap: Map[String, ((Node) => NodeSeq)] = {
    Map(
      "text" -> { e =>
        e.attribute("key") match {
          case Some(k) =>
            Text(LanguageUtil.get(k.toString))
          case None => Text("")
        }
      },
      "msgs" -> { e => Text("") })
  }

  def getOuterTemplate: NodeSeq = {
    XhtmlHelper.load(getOuterTemplatePath)
  }

  def getOuterTemplatePath: String = {
    Constants._pathPrefix + Constants.LOCAL_ACTION_TEMPLATE_FOLDER + File.separator + getOuterTemplateName + Constants.ACTION_TEMPLATE_SUFFIX;
  }

  def getOuterTemplateName: String = Constants.ACTION_DEFAULT_TEMPLATE

  def getTemplate: NodeSeq = {
    XhtmlHelper.load(getTemplatePath, "div")
  }

  def getTemplatePath: String = {
    Constants._pathPrefix + Constants.LOCAL_ACTION_TEMPLATE_FOLDER + basePath + getTemplateName + Constants.ACTION_TEMPLATE_SUFFIX;
  }

  def getTemplateName: String;

  def getContent: NodeSeq = {
    getTemplate
  }

  def getPageInfo: Map[String, String] = {
    Map[String, String](
      (Constants.KEY_BASE_PATH -> this.basePath))
  }
  
  protected def nodeFilter (xml:NodeSeq):NodeSeq = {
    xml
  }
}