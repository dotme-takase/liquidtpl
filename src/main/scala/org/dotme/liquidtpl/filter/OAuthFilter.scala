/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.filter


import com.google.appengine.api.users.User
import java.io.IOException;

import java.util.logging.Logger
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scala.collection.JavaConversions._
import com.google.appengine.api.oauth.OAuthService
import com.google.appengine.api.oauth.OAuthServiceFactory


class OAuthFilter extends AuthFilter{
  val logger:Logger = Logger.getLogger(classOf[OAuthFilter].getName)
  var pathRegex:String = null

  @throws(classOf[ServletException])
  override def init(config:FilterConfig):Unit =  {
    val params = config.getInitParameterNames
    pathRegex = config.getInitParameter("pathRegex")
    super.init(config)
  }

  /**
   * @param thisURL
   * @return
   */
  def matchPath(thisURL:String):Boolean = {
    val result:Boolean = if((pathRegex != null) && (pathRegex.size > 0)){
      !isExcludePath(thisURL) && thisURL.matches(pathRegex)
    } else {
      !isExcludePath(thisURL)
    }
    logger.info("URL:Pettern:Result = %s:%s:%s".format(thisURL, pathRegex, result))
    result
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doFilter( request:ServletRequest,  response:ServletResponse,
                        chain:FilterChain):Unit = {
    val thisURL:String = (request.asInstanceOf[HttpServletRequest]).getRequestURI();
    if (matchPath(thisURL) && !isLoggedIn(request.asInstanceOf[HttpServletRequest])) {
      response.asInstanceOf[HttpServletResponse].sendError(HttpServletResponse.SC_FORBIDDEN);
      return;
    }
    chain.doFilter(request, response);
  }

  override protected def isLoggedIn(request:HttpServletRequest):Boolean = {
    val oauthService:OAuthService = OAuthServiceFactory.getOAuthService();
    try {
      val user:User = oauthService.getCurrentUser
      logger.info("OAuth filter: %s".format(user.getEmail))
      true
    } catch {
      case e:Exception =>
        logger.warning(e.getMessage)
        logger.warning(e.getStackTraceString)
        false
    }
  }
}
