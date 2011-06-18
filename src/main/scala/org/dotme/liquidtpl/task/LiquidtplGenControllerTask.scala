/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.task

import java.io.File
import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.dotme.liquidtpl.generator.LiquidtplControllerGenerator
import org.dotme.liquidtpl.generator.LiquidtplControllerTestCaseGenerator
import org.slim3.gen.ClassConstants;
import org.slim3.gen.Constants;
import org.slim3.gen.desc.ControllerDesc;
import org.slim3.gen.desc.ControllerDescFactory;
import org.slim3.gen.generator.Generator;
import org.slim3.gen.task.WebConfig;

class LiquidtplGenControllerTask extends AbstractLiquidtplGenFileTask {

  /** the controller path */
  protected var controllerPath:String = "";

  /** the superclass name */
  protected var superclassName:String = ClassConstants.Controller;

  /** the superclass name of testcase */
  protected var testCaseSuperclassName:String = ClassConstants.ControllerTestCase;

  /** {@code true} if the controller uses a view */
  protected var useView:Boolean = false;

  /**
   * Sets the controllerPath.
   *
   * @param controllerPath
   *            the controllerPath to set
   */
  def setControllerPath(controllerPath:String):Unit = {
    this.controllerPath = controllerPath;
  }

  /**
   * Sets the superclassName.
   *
   * @param superclassName
   *            the superclassName to set
   */
  def setSuperclassName(superclassName:String):Unit = {
    this.superclassName = superclassName;
  }

  /**
   * Sets the superclass name of testcase.
   *
   * @param testCaseSuperclassName
   *            the superclass name of testcase to set
   */
  def setTestCaseSuperclassName(testCaseSuperclassName:String):Unit = {
    this.testCaseSuperclassName = testCaseSuperclassName;
  }

  /**
   * Sets the useView.
   *
   * @param useView
   *            the useView to set
   */
  def setUseView(useView:Boolean):Unit = {
    this.useView = useView;
  }

  @throws(classOf[Exception])
  override def doExecute() = {
    super.doExecute();
    if (controllerPath == null) {
      throw new IllegalStateException(
        "The controllerPath parameter is null.");
    }

    val controllerDesc:ControllerDesc = createControllerDesc();

    val scalaFile:LiquidtplFile = createLiquidtplFile(controllerDesc);
    val generator:Generator = createControllerGenerator(controllerDesc);
    generateLiquidtplFile(generator, scalaFile);

    val testCaseScalaFile:LiquidtplFile = createTestCaseLiquidtplFile(controllerDesc);
    val testCaseGenerator:Generator =
      createControllerTestCaseGenerator(controllerDesc);
    generateLiquidtplFile(testCaseGenerator, testCaseScalaFile);
  }

  /**
   * Creates a controller description.
   *
   * @return a controller description
   * @throws IOException
   * @throws XPathExpressionException
   */
  @throws(classOf[IOException])
  @throws(classOf[XPathExpressionException])
  private def createControllerDesc():ControllerDesc = {
    val path:String =
      if (controllerPath.startsWith("/") ) {
        controllerPath
      } else {
        "/" + controllerPath;
      }
    val controllerBasePackageName:String = getControllerBasePackageName();
    val factory:ControllerDescFactory =
      createControllerDescFactory(controllerBasePackageName);
    factory.createControllerDesc(path);
  }

  /**
   * Creates a controller base package name.
   *
   * @return a controller base package name
   * @throws IOException
   * @throws XPathExpressionException
   */
  @throws(classOf[IOException])
  @throws(classOf[XPathExpressionException])
  private def getControllerBasePackageName():String = {
    val buf:StringBuilder = new StringBuilder();
    val config:WebConfig = createWebConfig();
    buf.append(config.getRootPackageName());
    if (config.isGWTServiceServletDefined()) {
      buf.append(".");
      buf.append(Constants.SERVER_PACKAGE);
    }
    buf.append(".");
    buf.append(Constants.CONTROLLER_PACKAGE);
    return buf.toString();
  }

  /**
   * Creates a {@link ControllerDescFactory}.
   *
   * @param controllerBasePackageName
   *            the base package name of controllers.
   * @return a factory
   */
  protected def createControllerDescFactory(
    controllerBasePackageName:String):ControllerDescFactory = {
    return new ControllerDescFactory(
      controllerBasePackageName,
      superclassName,
      testCaseSuperclassName,
      useView);
  }

  /**
   * Creates a {@link Generator}.
   *
   * @param controllerDesc
   *            the controller description
   * @return a generator
   */
  protected def createControllerGenerator(controllerDesc:ControllerDesc):Generator = {
    return new LiquidtplControllerGenerator(controllerDesc);
  }

  /**
   * Creates a {@link Generator} for a test case.
   *
   * @param controllerDesc
   *            the controller description
   * @return a generator
   */
  protected def createControllerTestCaseGenerator(
    controllerDesc:ControllerDesc):Generator = {
    return new LiquidtplControllerTestCaseGenerator(controllerDesc);
  }

}
