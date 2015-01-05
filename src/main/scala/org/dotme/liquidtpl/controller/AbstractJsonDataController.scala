/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller
import scala.collection.JavaConversions._
import dispatch.classic.json.JsObject
import dispatch.classic.json.JsString
import dispatch.classic.json.JsValue
import sjson.json.JsonSerialization._
import sjson.json.DefaultProtocol._
import org.dotme.liquidtpl.Constants
import org.dotme.liquidtpl.LanguageUtil

abstract class AbstractJsonDataController extends AbstractJsonController with ControllerError {
  override def getJson: JsValue = {
    val result = request.getParameter(Constants.KEY_MODE) match {
      case Constants.MODE_LIST =>
        val values = getList match { case null => JsObject(); case json => json }
        if (existsError) {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_FAILURE)),
            (JsString(Constants.KEY_ERRORS), tojson(getErrorList))))
        } else {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
            (JsString(Constants.KEY_VALUES), values),
            (JsString(Constants.KEY_EMPTY_MESSAGE), tojson(LanguageUtil.get("error.dataNotFound")))))
        }
      case Constants.MODE_DETAIL =>
        val id: String = request.getParameter(Constants.KEY_ID);
        val values = getDetail(id) match { case null => JsObject(); case json => json }
        if (existsError) {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_FAILURE)),
            (JsString(Constants.KEY_ERRORS), tojson(getErrorList))))
        } else {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
            (JsString(Constants.KEY_VALUES), values),
            (JsString(Constants.KEY_ID) -> tojson(id))))
        }
      case Constants.MODE_FORM =>
        val id: String = request.getParameter(Constants.KEY_ID);
        val values = getForm(id) match { case null => JsObject(); case json => json }
        if (existsError) {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_FAILURE)),
            (JsString(Constants.KEY_ERRORS), tojson(getErrorList))))
        } else {
          JsObject(List(
            (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
            (JsString(Constants.KEY_VALUES), values),
            (JsString(Constants.KEY_ID) -> tojson(id)),
            (JsString(Constants.KEY_SUBMIT) -> tojson(LanguageUtil.get("save")))))
        }
      case _ => JsObject()
    }
    result
  }

  def getList: JsValue

  def getDetail(i: String): JsValue

  def getForm(id: String): JsValue

}
