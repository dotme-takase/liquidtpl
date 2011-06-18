/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl

import java.io.File

object Constants {
  /** Local file path*/
  var _pathPrefix=""
  val LINE_SEPARATOR:String = System.getProperty("line.separator")
  val LOCAL_ACTION_TEMPLATE_FOLDER = {
    val buf:StringBuilder = new StringBuilder
    buf
    .append("templates")
    .toString
  }

  /** Action Template*/
  val TAG_NAMESPACE:String = "liquidtpl"
  val ACTION_TEMPLATE_SUFFIX = ".html"
  val ACTION_INDEX_TEMPLATE:String = "index"
  val ACTION_DEFAULT_TEMPLATE = {
    val buf:StringBuilder = new StringBuilder
    buf.append("outer")
    .append(File.separator)
    .append("default")
    .toString
  }

  /** html parse*/
  val CHARSET:String = "utf-8"
  val HEADER_CONTENT_TYPE:String = "Content-Type"
  val HEADER_CONTENT_TYPE_JAVASCRIPT:String = "text/javascript; charset=" + CHARSET
  val HEADER_CONTENT_TYPE_JSON:String = "text/json; charset=" + CHARSET
  val HEADER_CONTENT_TYPE_TEXT:String = "text/plain; charset=" + CHARSET
  val HEADER_CONTENT_TYPE_HTML:String = "text/html; charset=" + CHARSET
  val HEADER_CONTENT_TYPE_XML:String = "text/xml; charset=" + CHARSET

  /** Request mode */
  val KEY_MODE = "mode"

  /** Key to prevent from CSRF */
  val KEY_CSRF = "csrf"

  /** Response result */
  val KEY_RESULT = "result"

  /** Response errors */
  val KEY_ERRORS = "errors"

  /** Response Global Error */
  val KEY_GLOBAL_ERROR = "grobal"

  /** Submit button name */
  val KEY_SUBMIT = "submit"

  /** Form Action url */
  val KEY_ACTION_URL = "url"

  /** Result Key redirect */
  val KEY_REDIRECT = "redirect"

  /** Common Json Data Key - key of map item */
  val KEY_KEY = "key"

  /** Common Json Data Key - value of map item */
  val KEY_VALUE = "value"

  /** Common Json Data Key - values from impremented action */
  val KEY_VALUES = "values"

  /** Common Json Data Key - extra informations from impremented action */
  val KEY_EXTRA_INFORMATION = "info"

  /** Common Json Data Key - list cursor current key extra informations from impremented action */
  val KEY_CURSOR_CURRENT = "cur"

  /** Common Json Data Key - list cursor next key extra informations from impremented action */
  val KEY_CURSOR_NEXT = "curnext"

  /** Common Json Data Key - list cursor previous key extra informations from impremented action */
  val KEY_CURSOR_PREVIOUS = "curprev"


  /** Common Json Data Key - Entity Key */
  val KEY_ID = "id"

  /** Common Json Data Key - Entity */
  val KEY_ENTITY = "entity"

  /** Common Json Data Key - basePath */
  val KEY_BASE_PATH = "basePath"

  /** Message when data is empty */
  val KEY_EMPTY_MESSAGE = "empty"

  /** Message when data is deleteConfirm */
  val KEY_DELETE_CONFORM = "deleteConfirm"

  /** Request mode - serve data to client */
  val MODE_LIST = "list"

  /** Request mode - serve data to client */
  val MODE_DETAIL = "detail"

  /** Request mode - serve data to client */
  val MODE_FORM = "form"

  /** Request mode - submit data to server */
  val MODE_SUBMIT = "submit"

  /** Response result - success */
  val RESULT_SUCCESS = "success"

  /** Response result - failure */
  val RESULT_FAILURE = "failure"

  /** common javascript variable name pageInfo */
  val JSVALUE_PAGE_INFO = "liquidtplPageInfo"

  /** common javascript variable name csrfKey */
  val JSVALUE_CSRF_KEY= "liquidtplCsrfKey"
}
