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

import gr.spinellis.ckjm.PackageMetrics;
import gr.spinellis.ckjm.report.CkjmOutputHandler;
import gr.spinellis.ckjm.ClassMetrics;

/**
 * Simple plain text output formatter
 *
 * @author Julien Rentrop
 */
public class PrintPlainResults implements CkjmOutputHandler {
  private PrintStream p;

  public PrintPlainResults(PrintStream p) {
    this.p = p;
  }

  @Override
  public void handlePackage(PackageMetrics p) {
  }

  @Override
  public void endOfPackage(PackageMetrics p) {
  }

  @Override
  public void printHeader() {
  }

  @Override
  public void printFooter() {
  }

  public void handleClass(ClassMetrics c) {
    p.println(c.getClassName() + " " + c.toString());
  }
}
