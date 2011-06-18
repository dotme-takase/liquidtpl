/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dotme.liquidtpl.generator

import org.slim3.gen.desc.ControllerDesc
import org.slim3.gen.generator.Generator
import org.slim3.gen.printer.Printer

class LiquidtplControllerTestCaseGenerator(controllerDesc:ControllerDesc) extends Generator {

  val SpecsSpecification:String = "org.specs.Specification";
  val SpecsRunner:String = "org.specs.runner";
  val TesterPackage:String = "org.slim3.tester.";
  val ControllerTester:String = "ControllerTester";

  if (controllerDesc == null) {
    throw new NullPointerException(
      "The controllerDesc parameter is null.");
  }

  def generate(p:Printer):Unit = {
    if (controllerDesc.getPackageName().length() != 0) {
      p.println("package %s", controllerDesc.getPackageName());
      p.println();
    }

    p.println("import %s", SpecsSpecification );
    p.println("import %s._", SpecsRunner );
    p.println("import %s%s", TesterPackage, ControllerTester );
    p.println();
    p.print(
      "object %sSpec extends %s",
      controllerDesc.getSimpleName(),
      SpecsSpecification );
    p.println(" {");
    p.println();
    p.println("  val tester = new %s( classOf[%s] )",
              ControllerTester,
              controllerDesc.getSimpleName() );
    p.println();
    p.println("  \"%s\" should {",controllerDesc.getSimpleName() );
    p.println("    doBefore{ tester.setUp;tester.start(\"%s\")}", controllerDesc.getPath());
    p.println();
    p.println("    \"not null\" >> {");
    p.println("      val controller = tester.getController[%s]" , controllerDesc.getSimpleName());
    p.println("      controller mustNotBe null");
    p.println("    }");
    p.println("    \"not redirect\" >> {");
    p.println("      tester.isRedirect mustBe false");
    p.println("    }");

    if (controllerDesc.isUseView()) {
      p.println("    \"get destination path is %s\" >> {",controllerDesc.getViewName());
      p.println("      tester.getDestinationPath must_==/ \"%s\"", controllerDesc.getViewName() );
      p.println("    }");
    } else {
      p.println("    \"get destination path is null\" >> {" );
      p.println("      tester.getDestinationPath mustBe null" );
      p.println("    }");
    }
    p.println();
    p.println("    doAfter{ tester.tearDown}");
    p.println();
    p.println("    \"after tearDown\" >> {");
    p.println("        true");
    p.println("    }");
    p.println("  }");
    p.println("}");

    p.println("class %sSpecTest extends JUnit4( %sSpec )", controllerDesc.getSimpleName() , controllerDesc.getSimpleName());
  }
}
