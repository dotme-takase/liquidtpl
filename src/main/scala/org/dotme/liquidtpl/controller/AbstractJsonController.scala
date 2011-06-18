/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller
import scala.collection.JavaConversions._
import dispatch.json.JsValue
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.dotme.liquidtpl.helper.BasicHelper

abstract class AbstractJsonController extends Controller  {
  @throws(classOf[Exception])
  override protected def run():Navigation = {
    BasicHelper.writeJsonCommentFiltered(response, getJson)
    return null;
  }

  def getJson:JsValue
}
