/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller

import org.dotme.liquidtpl.Constants
import org.dotme.liquidtpl.LanguageUtil
import org.dotme.liquidtpl.helper.BasicHelper
import dispatch.json.JsObject
import dispatch.json.JsString
import java.util.logging.Logger
import org.slim3.controller.Navigation
import sjson.json.JsonSerialization
import sjson.json.JsonSerialization._
import sjson.json.DefaultProtocol._

abstract class AbstractFormController extends AbstractActionController with ControllerError {
  override val logger = Logger.getLogger(classOf[AbstractFormController].getName)

  @throws(classOf[Exception])
  override protected def run(): Navigation = {
    request.getParameter(Constants.KEY_MODE) match {
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
        extraInformation.clear
        null
      }
      case null => {
        //render form template
        super.run
      }
      case _ => {
        val values = JsObject(List(
          (JsString(Constants.KEY_RESULT), tojson(Constants.RESULT_FAILURE)),
          (JsString(Constants.KEY_EXTRA_INFORMATION) -> tojson(extraInformation.toMap)),
          (JsString(Constants.KEY_ERRORS), tojson(Map(Constants.KEY_GLOBAL_ERROR -> LanguageUtil.get("error.dataNotFound"))))))
        BasicHelper.writeJsonCommentFiltered(response, values)
        extraInformation.clear
        null
      }
    }
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