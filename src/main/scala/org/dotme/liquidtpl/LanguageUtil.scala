/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl

import java.io.File
import java.text.MessageFormat
import java.util.Locale
import java.util.ResourceBundle

object LanguageUtil {
  val LANGUAGE_FOLDER = "contents"
  val LANGUAGE_FILE = "language"
  val LANGUAGE_DEFAULT = "ja"
  private def languagePath:String = {
    LANGUAGE_FOLDER + File.separator + LANGUAGE_FILE
  }
  def get(key:String, args:Option[Array[Object]], locale:Locale):String = {
    var message:String = "undefined"
    try{
      message = ResourceBundle.getBundle(languagePath, locale).getString(key)
      args match {
        case None => message
          case Some(v) => (new MessageFormat(message)).format(v)
      }
    } catch {
      case e:Exception => println(e); message
    }
  }
  
  def get(key:String, args:Option[Array[Object]]):String = {
    var locale:Locale = new Locale(LANGUAGE_DEFAULT)
    get(key, args, locale);
  }

  def get(key:String):String = {
    get(key, None);
  }
}