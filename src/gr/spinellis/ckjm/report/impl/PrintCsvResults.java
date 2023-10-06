package gr.spinellis.ckjm.report.impl;

import java.io.PrintStream;

import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.report.CkjmOutputHandler;

public class PrintCsvResults implements CkjmOutputHandler {
  private PrintStream p;

  public PrintCsvResults(PrintStream p) {
    this.p = p;
  }

  public void printHeader() {
    p.println("name,package,wmc,dit,noc,cbo,rfc,lcom,ca,npm,depends_on");
  }

  private String getPackage(String clz) {
    int lastDot = clz.lastIndexOf('.');
    return lastDot > -1 ? clz.substring(0, lastDot) : "";
  }

  public void handleClass(String name, ClassMetrics c) {
    for (String clz : c.getEfferentCoupledClasses()) {
      StringBuilder ln = new StringBuilder();
      ln.append(name).append(',')
          .append(getPackage(name)).append(',')
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
}
