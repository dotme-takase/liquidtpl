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

abstract class AbstractJsonDataController extends AbstractJsonController with ControllerError {
  override def getJson: JsValue = {
    val result = request.getParameter(Constants.KEY_MODE) match {
      case Constants.MODE_LIST =>
        JsObject(List(
          (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
          (JsString(Constants.KEY_VALUES), getList),
          (JsString(Constants.KEY_EMPTY_MESSAGE), tojson(LanguageUtil.get("error.dataNotFound")))))

      case Constants.MODE_DETAIL =>
        val id: String = request.getParameter(Constants.KEY_ID);
        JsObject(List(
          (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
          (JsString(Constants.KEY_VALUES), getDetail(id)),
          (JsString(Constants.KEY_ID) -> tojson(id))))

      case Constants.MODE_FORM =>
        val id: String = request.getParameter(Constants.KEY_ID);
        JsObject(List(
          (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_SUCCESS)),
          (JsString(Constants.KEY_VALUES), getForm(id)),
          (JsString(Constants.KEY_ID) -> tojson(id)),
          (JsString(Constants.KEY_SUBMIT) -> tojson(LanguageUtil.get("save")))))
      case _ => JsObject()
    }
    result
  }

  def getList: JsValue

  def getDetail(i: String): JsValue

  def getForm(id: String): JsValue

}
