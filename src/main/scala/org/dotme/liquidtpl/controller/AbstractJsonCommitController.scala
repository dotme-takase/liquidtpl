/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller
import scala.collection.JavaConversions._
import dispatch.json.JsObject
import dispatch.json.JsString
import dispatch.json.JsValue
import sjson.json.JsonSerialization._
import sjson.json.DefaultProtocol._
import org.dotme.liquidtpl.Constants
import org.dotme.liquidtpl.LanguageUtil
import org.dotme.liquidtpl.helper.BasicHelper

abstract class AbstractJsonCommitController extends AbstractJsonController with ControllerError {
  override def getJson: JsValue = {
    val result = request.getParameter(Constants.KEY_MODE) match {
      case Constants.MODE_SUBMIT => {
        val values = if (validate() && update()) {
          if (redirectUri == null || redirectUri.size == 0) {
            JsObject(List(
              (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
              (JsString(Constants.KEY_EXTRA_INFORMATION) -> tojson(extraInformation.toMap))))
          } else {
            JsObject(List(
              (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
              (JsString(Constants.KEY_EXTRA_INFORMATION) -> tojson(extraInformation.toMap)),
              (JsString(Constants.KEY_REDIRECT), tojson(redirectUri))))
          }
        } else {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_FAILURE)),
            (JsString(Constants.KEY_EXTRA_INFORMATION) -> tojson(extraInformation.toMap)),
            (JsString(Constants.KEY_ERRORS), tojson(getErrorList))))
        }
        BasicHelper.writeJsonCommentFiltered(response, values)
        null
      }
      case _ => JsObject()
    }
    extraInformation.clear
    result
  }

  //KEY_EXTRA_INFORMATION
  private val extraInformation: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map[String, String]()

  def putExtraInformation(key: String, value: String): Unit = {
    extraInformation.put(key, value)
  }

  def redirectUri: String = null;
  def validate(): Boolean
  def update(): Boolean

}
