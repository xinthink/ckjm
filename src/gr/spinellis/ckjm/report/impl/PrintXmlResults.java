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
import gr.spinellis.ckjm.report.CkjmOutputHandler;

/**
 * XML output formatter
 *
 * @author Julien Rentrop
 */
public class PrintXmlResults implements CkjmOutputHandler {
  protected final PrintStream p;

  public PrintXmlResults(PrintStream p) {
    this.p = p;
  }

  @Override
  public void printHeader() {
    p.println("<?xml version=\"1.0\"?>");
    p.println("<ckjm>");
  }

  @Override
  public void handlePackage(PackageMetrics p) {
    StringBuilder efferent = new StringBuilder();
    efferent.append("<dependsOn>");
    for (String clz : p.getEfferentCoupledClasses()) {
      efferent.append("<class>")
          .append(clz)
          .append("</class>");
    }
    efferent.append("</dependsOn>");

    StringBuilder afferent = new StringBuilder();
    afferent.append("<dependedBy>");
    for (String clz : p.getAfferentCoupledClasses()) {
      afferent.append("<class>")
          .append(clz)
          .append("</class>");
    }
    afferent.append("</dependedBy>");

    this.p.print("<package>\n" +
        "<name>" + p.getPackageName() + "</name>\n" +
        "<cbo>" + p.getCe() + "</cbo>\n" +
        "<ca>" + p.getCa() + "</ca>\n" +
        efferent + "\n" +
        afferent + "\n");
  }

  @Override
  public void endOfPackage(PackageMetrics p) {
    this.p.println("</package>");
  }

  @Override
  public void handleModule(ModuleMetrics m) {
  }

  @Override
  public void endOfModule(ModuleMetrics m) {
  }

  @Override
  public void handleClass(ClassMetrics c) {
    StringBuilder efferent = new StringBuilder();
    efferent.append("<dependsOn>");
    for (String clz : c.getEfferentCoupledClasses()) {
      efferent.append("<class>")
          .append(clz)
          .append("</class>");
    }
    efferent.append("</dependsOn>");

    StringBuilder afferent = new StringBuilder();
    afferent.append("<dependedBy>");
    for (String clz : c.getAfferentCoupledClasses()) {
      afferent.append("<class>")
          .append(clz)
          .append("</class>");
    }
    afferent.append("</dependedBy>");

    p.print("<class>\n" +
        "<name>" + c.getClassName() + "</name>\n" +
        "<wmc>" + c.getWmc() + "</wmc>\n" +
        "<dit>" + c.getDit() + "</dit>\n" +
        "<noc>" + c.getNoc() + "</noc>\n" +
        "<cbo>" + c.getCbo() + "</cbo>\n" +
        "<rfc>" + c.getRfc() + "</rfc>\n" +
        "<lcom>" + c.getLcom() + "</lcom>\n" +
        "<ca>" + c.getCa() + "</ca>\n" +
        "<npm>" + c.getNpm() + "</npm>\n" +
        efferent + "\n" +
        afferent + "\n" +
        "</class>\n");
  }

  @Override
  public void printFooter() {
    p.println("</ckjm>");
  }
}
