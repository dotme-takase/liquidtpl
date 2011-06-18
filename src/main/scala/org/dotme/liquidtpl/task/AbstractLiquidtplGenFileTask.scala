/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.task

import java.io.File
import java.io.IOException
import org.slim3.gen.desc.ClassDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer
import org.slim3.gen.task.AbstractGenJavaFileTask

abstract class AbstractLiquidtplGenFileTask extends AbstractGenJavaFileTask {

  protected var srcScalaDir:File = null;
  protected var testScalaDir:File = null;

  /**
   * Sets the srcScalaDir.
   *
   * @param srcScalaDir
   *            the srcScalaDir to set
   */
  def setSrcScalaDir(srcScalaDir:File):Unit = {
    this.srcScalaDir = srcScalaDir;
  }

  /**
   * Sets the testScalaDir.
   *
   * @param testScalaDir
   *            the testScalaDir to set
   */
  def setTestScalaDir(testScalaDir:File):Unit = {
    this.testScalaDir = testScalaDir;
  }

  @throws(classOf[Exception])
  override protected def doExecute():Unit = {
    super.doExecute();
    if (srcScalaDir == null) {
      throw new IllegalStateException("The srcScalaDir parameter is null.");
    }
    if (testScalaDir == null) {
      throw new IllegalStateException("The testScalaDir parameter is null.");
    }
  }

  /**
   * Creates a scala file.
   *
   * @param classDesc
   *            the class description
   * @return a sacla file.
   */
  protected def createLiquidtplFile(classDesc:ClassDesc):LiquidtplFile = {
    new LiquidtplFile(srcScalaDir, classDesc);
  }

  /**
   * Creates a scala file of test case.
   *
   * @param classDesc
   *            the class description
   * @return a scala file.
   */
  protected def createTestCaseLiquidtplFile(classDesc:ClassDesc):LiquidtplFile = {
    return new LiquidtplFile(testScalaDir, classDesc, "Spec");
  }

  /**
   * Generates a scala file.
   *
   * @param generator
   *            the generator
   * @param LiquidtplFile
   *            the scala file to be generated
   * @throws IOException
   */
  @throws(classOf[IOException])
  protected def generateLiquidtplFile(generator:Generator
                                      , liquidtplFile:LiquidtplFile):Unit = {
    val file:File = liquidtplFile.getFile();
    val className:String = liquidtplFile.getClassName();
    if (file.exists()) {
      log( "Already exists. Generation Skipped. ({%s}.scala:0)"
          .format(className)
      );
      return;
    }
    var printer:Printer = null;
    try {
      printer = createPrinter(file);
      generator.generate(printer);
    } finally {
      if (printer != null) {
        printer.close();
      }
    }
    log("Generated. ({%s}.scala:0)"
        .format(className));
  }
}
