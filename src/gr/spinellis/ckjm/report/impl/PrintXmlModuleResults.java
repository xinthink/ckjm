/*
 * (C) Copyright 2005 Diomidis Spinellis, Julien Rentrop
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package gr.spinellis.ckjm.report.impl;

import java.io.PrintStream;

import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.ModuleMetrics;
import gr.spinellis.ckjm.PackageMetrics;

/**
 * XML output formatter
 *
 * @author Julien Rentrop
 */
public class PrintXmlModuleResults extends PrintXmlResults {

  public PrintXmlModuleResults(PrintStream p) {
    super(p);
  }

  @Override
  public void handlePackage(PackageMetrics p) {
  }

  @Override
  public void endOfPackage(PackageMetrics p) {
  }

//  @Override
//  public void handleClass(ClassMetrics c) {
//  }

  @Override
  public void handleModule(ModuleMetrics m) {
    StringBuilder efferent = new StringBuilder();
    efferent.append("<dependsOn>");
    for (String dep : m.getEfferentCoupledModules()) {
      efferent.append("<module>")
          .append(dep)
          .append("</module>");
    }
    efferent.append("</dependsOn>");

//    StringBuilder afferent = new StringBuilder();
//    afferent.append("<dependedBy>");
//    for (String clz : m.getAfferentCoupledClasses()) {
//      afferent.append("<class>")
//          .append(clz)
//          .append("</class>");
//    }
//    afferent.append("</dependedBy>");

    this.p.print("<module>\n" +
        "<name>" + m.getModuleName() + "</name>\n" +
        "<cbo>" + m.getCe() + "</cbo>\n" +
        "<ca>" + m.getCa() + "</ca>\n" +
        efferent + "\n");
  }

  @Override
  public void endOfModule(ModuleMetrics m) {
    this.p.println("</module>");
  }

}
