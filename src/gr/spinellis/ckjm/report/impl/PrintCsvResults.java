package gr.spinellis.ckjm.report.impl;

import java.io.PrintStream;

import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.PackageMetrics;
import gr.spinellis.ckjm.report.CkjmOutputHandler;

public class PrintCsvResults implements CkjmOutputHandler {
  private PrintStream p;

  public PrintCsvResults(PrintStream p) {
    this.p = p;
  }

  @Override
  public void printHeader() {
    p.println("name,type,package,wmc,dit,noc,cbo,rfc,lcom,ca,npm,depends_on");
  }

  @Override
  public void handlePackage(PackageMetrics p) {
    for (String clz : p.getEfferentCoupledClasses()) {
      StringBuilder ln = new StringBuilder();
      ln.append(p.getPackageName()).append(',')
          .append("package").append(',')
          .append(p.getPackageName()).append(',')
          .append(',')
          .append(',')
          .append(',')
          .append(p.getCe()).append(',')
          .append(',')
          .append(',')
          .append(p.getCa()).append(',')
          .append(',')
          .append(clz);
      this.p.println(ln);
    }
  }

  @Override
  public void endOfPackage(PackageMetrics p) {
  }

  @Override
  public void handleClass(ClassMetrics c) {
    for (String clz : c.getEfferentCoupledClasses()) {
      StringBuilder ln = new StringBuilder();
      ln.append(c.getClassName()).append(',')
          .append("class").append(',')
          .append(c.getPackageName()).append(',')
          .append(c.getWmc()).append(',')
          .append(c.getDit()).append(',')
          .append(c.getNoc()).append(',')
          .append(c.getCbo()).append(',')
          .append(c.getRfc()).append(',')
          .append(c.getLcom()).append(',')
          .append(c.getCa()).append(',')
          .append(c.getNpm()).append(',')
          .append(clz);
      p.println(ln);
    }
  }

  @Override
  public void printFooter() {
  }
}
