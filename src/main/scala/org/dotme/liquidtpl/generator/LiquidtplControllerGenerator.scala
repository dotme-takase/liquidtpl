/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.generator

import org.slim3.gen.ClassConstants
import org.slim3.gen.desc.ControllerDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer
import org.slim3.gen.util.ClassUtil

class LiquidtplControllerGenerator(controllerDesc:ControllerDesc) extends Generator {

  if (controllerDesc == null) {
    throw new NullPointerException(
      "The controllerDesc parameter is null.");
  }

  def generate(p:Printer):Unit = {
    if (controllerDesc.getPackageName().length() != 0) {
      p.println("package %s", controllerDesc.getPackageName());
      p.println();
    }
    p.println("import %s", controllerDesc.getSuperclassName());
    p.println("import %s", ClassConstants.Navigation);
    p.println();
    p.println("class %s extends %s {", controllerDesc
              .getSimpleName(), ClassUtil.getSimpleName(controllerDesc
                                                        .getSuperclassName()));
    p.println();
    p.println("@throws(classOf[Exception])");
    p.println("    override def run():%s =  {", ClassUtil.getSimpleName(ClassConstants.Navigation) );
    if (controllerDesc.isUseView()) {
      p.println("        forward(\"%s\")", controllerDesc
                .getSimpleViewName());
    } else {
      p.println("        null;");
    }
    p.println("    }");
    p.println("}");
  }
}
