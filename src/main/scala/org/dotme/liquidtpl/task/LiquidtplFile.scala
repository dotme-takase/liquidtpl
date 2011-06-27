/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.task
import java.io.File;

import org.slim3.gen.desc.ClassDesc;
import org.slim3.gen.util.StringUtil;

/**
 * Creates a new {@link LiquidtplFile}.
 *
 * @param baseDir
 *            the base directory
 * @param classDesc
 *            the class description
 * @param suffix
 *            the suffix of class name
 */
class LiquidtplFile(baseDir: File, classDesc: ClassDesc, suffix: String) {

  val file: File = createFile(baseDir, classDesc, suffix);
  /**
   * Creates a new {@link LiquidtplFile}.
   *
   * @param baseDir
   *            the base directory
   * @param classDesc
   *            the class description
   */
  def this(baseDir: File, classDesc: ClassDesc) = this(baseDir, classDesc, "");

  /**
   * Creates a file.
   *
   * @param baseDir
   *            the base directory
   * @param classDesc
   *            the class description
   * @param suffix
   *            the suffix of class name
   * @return a file
   */
  protected def createFile(baseDir: File, classDesc: ClassDesc, suffix: String): File = {
    val packageName: String = classDesc.getPackageName
    val packageDir: File =
      if (packageName == null || packageName.size == 0) {
        baseDir;
      } else {
        new File(baseDir, packageName.replace(
          ".",
          File.separator));
      }
    mkdirs(packageDir);
    return new File(packageDir, classDesc.getSimpleName().replace('.', '/')
      + suffix
      + ".scala");
  }

  /**
   * Creates the directory, including any necessary but nonexistent parent
   * directories.
   *
   * @param dir
   *            the directory
   */
  protected def mkdirs(dir: File): Unit = {
    dir.mkdirs();
  }

  /**
   * Returns the class name.
   *
   * @return the class name
   */
  def getClassName(): String = {
    return classDesc.getQualifiedName() + suffix;
  }

  /**
   * Returns the file.
   *
   * @return the file
   */
  def getFile(): File = {
    return file;
  }
}