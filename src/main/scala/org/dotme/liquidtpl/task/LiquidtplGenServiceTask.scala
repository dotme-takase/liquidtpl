/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.task


import java.io.IOException
import javax.xml.xpath.XPathExpressionException
import org.dotme.liquidtpl.generator.LiquidServiceGenerator
import org.dotme.liquidtpl.generator.LiquidtplServiceTestCaseGenerator
import org.slim3.gen.ClassConstants
import org.slim3.gen.Constants
import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.task.ClassNameBuilder
import org.slim3.gen.task.WebConfig

class LiquidtplGenServiceTask extends AbstractLiquidtplGenFileTask {
  /** the packageName */
  protected var packageName:String = "";

  /** the superclass name */
  protected var superclassName:String = ClassConstants.Object;

  /** the superclass name of testcase */
  protected var testCaseSuperclassName:String =
    ClassConstants.AppEngineTestCase;

  /** the serviceDefinition */
  protected var serviceDefinition:String = "";

  /**
   * Sets the packageName.
   *
   * @param packageName
   *            the packageName to set
   */
  def setPackageName(packageName:String):Unit = {
    this.packageName = packageName;
  }

  /**
   * Sets the superclass name.
   *
   * @param superclassName
   *            the superclass name to set
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
   * Sets the serviceDefinition.
   *
   * @param serviceDefinition
   *            the serviceDefinition to set
   */
  def setServiceDefinition(serviceDefinition:String):Unit = {
    this.serviceDefinition = serviceDefinition;
  }

  @throws(classOf[Exception])
  override def doExecute():Unit = {
    super.doExecute();
    if (serviceDefinition == null) {
      throw new IllegalStateException(
        "The serviceDefinition parameter is null.");
    }

    val serviceDesc:ServiceDesc = createServiceDesc();

    val scalaFile:LiquidtplFile = createLiquidtplFile(serviceDesc);
    val generator:Generator = createServiceGenerator(serviceDesc);
    generateLiquidtplFile(generator, scalaFile);

    val testCaseLiquidtplFile:LiquidtplFile = createTestCaseLiquidtplFile(serviceDesc);
    val testCaseGenerator:Generator =
      createServiceTestCaseGenerator(serviceDesc);
    generateLiquidtplFile(testCaseGenerator, testCaseLiquidtplFile);
  }

  /**
   * Creates a service description.
   *
   * @return a GWT service implementation description
   * @throws IOException
   * @throws XPathExpressionException
   */
  @throws(classOf[IOException])
  @throws(classOf[XPathExpressionException])
  private def createServiceDesc():ServiceDesc = {
    val nameBuilder:ClassNameBuilder = new ClassNameBuilder();
    nameBuilder.append(getServiceBasePackageName());
    nameBuilder.append(serviceDefinition);

    val serviceDesc:ServiceDesc = new ServiceDesc();
    serviceDesc.setPackageName(nameBuilder.getPackageName());
    serviceDesc.setSimpleName(nameBuilder.getSimpleName());
    serviceDesc.setSuperclassName(superclassName);
    serviceDesc.setTestCaseSuperclassName(testCaseSuperclassName);
    return serviceDesc;
  }

  /**
   * Returns the service base package name.
   *
   * @return the service implementation base package name.
   * @throws IOException
   * @throws XPathExpressionException
   */
  @throws(classOf[IOException])
  @throws(classOf[XPathExpressionException])
  protected def getServiceBasePackageName():String = {
    if (packageName != null) {
      return packageName;
    }
    val config:WebConfig = createWebConfig();
    val buf:StringBuilder = new StringBuilder();
    buf.append(config.getRootPackageName());
    if (config.isGWTServiceServletDefined()) {
      buf.append(".");
      buf.append(Constants.SERVER_PACKAGE);
    }
    buf.append(".");
    buf.append(Constants.SERVICE_PACKAGE);
    return buf.toString();
  }

  /**
   * Creates a {@link Generator}.
   *
   * @param serviceDesc
   *            the service description
   * @return a generator
   */
  protected def createServiceGenerator(serviceDesc:ServiceDesc):Generator = {
    return new LiquidServiceGenerator(serviceDesc);
  }

  /**
   * Creates a {@link Generator} for a test case.
   *
   * @param serviceDesc
   *            the service description
   * @return a generator
   */
  protected def createServiceTestCaseGenerator(serviceDesc:ServiceDesc):Generator = {
    return new LiquidtplServiceTestCaseGenerator(serviceDesc);
  }
}
