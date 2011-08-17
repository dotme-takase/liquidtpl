/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller
import scala.collection.JavaConversions._
import dispatch.json.JsValue
import org.slim3.controller.Controller
import org.slim3.controller.Navigation
import org.dotme.liquidtpl.helper.BasicHelper
import dispatch.json.JsObject
import dispatch.json.JsString
import sjson.json.JsonSerialization._
import sjson.json.DefaultProtocol._
import org.dotme.liquidtpl.Constants

abstract class AbstractJsonController extends Controller {
  @throws(classOf[Exception])
  override protected def run(): Navigation = {
    val jsValue: JsValue = getJson
    val jsValueWithInfo: JsValue = jsValue match {
      case obj: JsObject =>
        JsObject(obj.self ++ Map((JsString(Constants.KEY_EXTRA_INFORMATION) -> JsObject(extraInformation.toMap))))
      case v => v
    }
    BasicHelper.writeJsonCommentFiltered(response, jsValueWithInfo)
    extraInformation.clear
    return null;
  }

  //KEY_EXTRA_INFORMATION
  private val extraInformation: scala.collection.mutable.Map[JsString, JsValue] = scala.collection.mutable.Map[JsString, JsValue]()

  def putExtraInformation(key: String, value: String): Unit = {
    extraInformation.put(JsString(key), JsString(value))
  }
  
  def putExtraInformation(key: String, value: JsValue): Unit = {
    extraInformation.put(JsString(key), value)
  }

  def getJson: JsValue
}
