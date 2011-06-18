/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.filter

import java.io.IOException;

import java.security.Principal
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory
import scala.collection.mutable.ListBuffer


class AuthFilter extends Filter{
  
  override def destroy() = {
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override def doFilter( request:ServletRequest,  response:ServletResponse,
                        chain:FilterChain):Unit = {
    val userService:UserService = UserServiceFactory.getUserService();
    val thisURL:String = (request.asInstanceOf[HttpServletRequest]).getRequestURI();
    if (!isExcludePath(thisURL) && !isLoggedIn(request.asInstanceOf[HttpServletRequest])) {
      (response.asInstanceOf[HttpServletResponse]).sendRedirect(
        userService.createLoginURL(thisURL)
      );
      return;
    }
    chain.doFilter(request, response);
  }

  /**
   * @param thisURL
   * @return
   */
  def isExcludePath(thisURL:String):Boolean = {
    if(thisURL.startsWith("/_ah/")){
      true
    } else {
      false
    }
  }

  @throws(classOf[ServletException])
  override def init(config:FilterConfig):Unit =  {
  }

  protected def isLoggedIn(request:HttpServletRequest):Boolean = {
    val userService:UserService = UserServiceFactory.getUserService();
    val principal:Principal = request.getUserPrincipal();
    if (principal == null || userService.isUserLoggedIn() == false) {
      false;
    } else {
      true
    }
  }
}
