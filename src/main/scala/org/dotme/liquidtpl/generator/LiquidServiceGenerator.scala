/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.generator

import org.slim3.gen.ClassConstants
import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer

class LiquidServiceGenerator(serviceDesc:ServiceDesc) extends Generator {
  if (serviceDesc == null) {
    throw new NullPointerException("The serviceDesc parameter is null.");
  }

  def generate(p:Printer):Unit = {
    if (serviceDesc.getPackageName().length() != 0) {
      p.println("package %s", serviceDesc.getPackageName());
      p.println();
    }
    if (!ClassConstants.Object.equals(serviceDesc.getSuperclassName())) {
      p.println("import %s", serviceDesc.getSuperclassName());
    }
    p.println();
    p.print("class %s", serviceDesc.getSimpleName());
    if (!ClassConstants.Object.equals(serviceDesc.getSuperclassName())) {
      p.print(" extends %s", serviceDesc.getSuperclassName());
    }
    p.println(" {");
    p.println();
    p.println("}");
  }
}
