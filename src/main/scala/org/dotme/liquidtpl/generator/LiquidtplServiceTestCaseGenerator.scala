/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.generator

import org.slim3.gen.desc.ServiceDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer

class LiquidtplServiceTestCaseGenerator(serviceDesc:ServiceDesc) extends Generator {

  val SpecsSpecification:String = "org.specs.Specification";
  val SpecsRunner:String = "org.specs.runner";
  val TesterPackage:String = "org.slim3.tester.";
  val AppEngineTester:String = "AppEngineTester";

  if (serviceDesc == null) {
    throw new NullPointerException("The serviceDesc parameter is null.");
  }

  def generate(p:Printer):Unit = {
    if (serviceDesc.getPackageName().length() != 0) {
      p.println("package %s", serviceDesc.getPackageName());
      p.println();
    }
    p.println("import %s", SpecsSpecification );
    p.println("import %s._", SpecsRunner );
    p.println("import %s%s", TesterPackage, AppEngineTester );
    p.println();
    p.print(
      "object %sSpec extends %s",
      serviceDesc.getSimpleName(),
      SpecsSpecification );
    p.println(" {");
    p.println("  val tester = new %s", AppEngineTester );
    p.println("  val service = new %s", serviceDesc.getSimpleName() );
    p.println();
    p.println("  \"%s\" should {",serviceDesc.getSimpleName() );
    p.println("    doBefore{ tester.setUp}");
    p.println();
    p.println("    \"not null\" >> {");
    p.println("      service mustNotBe null");
    p.println("    }");
    p.println();
    p.println("    doAfter{ tester.tearDown}");
    p.println();
    p.println("    \"after tearDown\" >> {");
    p.println("        true");
    p.println("    }");
    p.println("  }");
    p.println("}");

    p.println("class %sSpecTest extends JUnit4( %sSpec )", serviceDesc.getSimpleName() , serviceDesc.getSimpleName());
  }
}
