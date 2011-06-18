/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.controller

trait ControllerError {
  private val errorContainer:scala.collection.mutable.Map[String, String] =
    scala.collection.mutable.Map[String, String]();

  protected def addError(key:String, value:String):Unit = {
    errorContainer.put(key, value)
  }
  protected def existsError():Boolean = {
    errorContainer.size > 0
  }
  protected def getErrorList():Map[String, String] = {
    errorContainer.toMap
  }
}
