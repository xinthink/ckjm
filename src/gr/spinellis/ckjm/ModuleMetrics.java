package gr.spinellis.ckjm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ModuleMetrics {

  private final String moduleName;

  /**
   * The set of class metrics.
   */
  private final Set<ClassMetrics> classMetrics = new TreeSet<>();

  /**
   * Coupled classes: classes that use any class in this package
   */
  private final Set<String> afferentCoupledClasses;

  /**
   * Coupled classes: classes used by any class in this package
   */
  private final Set<String> efferentCoupledClasses;

  private final Set<String> efferentCoupledModules = new HashSet<>();

  public ModuleMetrics(String moduleName) {
    this.moduleName = moduleName;
    afferentCoupledClasses = new HashSet<>();
    efferentCoupledClasses = new HashSet<>();
  }

  /**
   * Return the name of this package.
   */
  public String getModuleName() {
    return moduleName;
  }

  /**
   * Return the set of classes in this package.
   */
  public Set<ClassMetrics> getClassMetrics() {
    return classMetrics;
  }

  /**
   * Add a class (metrics) to this package.
   */
  public void addClassMetrics(ClassMetrics cm) {
    classMetrics.add(cm);
  }

  /**
   * Return the set of classes in this package.
   */
  public Set<String> getAfferentCoupledClasses() {
    return afferentCoupledClasses;
  }

  /**
   * Return the set of classes used by any class in this package.
   */
  public Set<String> getEfferentCoupledClasses() {
    return efferentCoupledClasses;
  }

  /**
   * Return the Ca metric for this package.
   */
  public int getCa() {
    return afferentCoupledClasses.size();
  }

  /**
   * Return the Ce metric for this package.
   */
  public int getCe() {
    return efferentCoupledClasses.size();
  }

  public Set<String> getEfferentCoupledModules() {
    return efferentCoupledModules;
  }

  public void calculate(Map<String, String> classModules) {
    for (ClassMetrics cm : classMetrics) {
      updateCa(cm, classModules);
      updateCe(cm, classModules);
    }
  }

  /**
   * Refresh the package level EC metric with the given child ClassMetrics.
   */
  private void updateCe(ClassMetrics cm, Map<String, String> classModules) {
    for (String className : cm.getEfferentCoupledClasses()) {
      String theModule = classModules.get(className);
      if (!moduleName.equals(theModule)) {
        efferentCoupledClasses.add(className);
        efferentCoupledModules.add(theModule);
      }
    }
  }

  /**
   * Refresh the package level AC metric with the given child ClassMetrics.
   */
  private void updateCa(ClassMetrics cm, Map<String, String> classModules) {
    for (String className : cm.getAfferentCoupledClasses()) {
      String theModule = classModules.get(className);
      if (!moduleName.equals(theModule)) {
        afferentCoupledClasses.add(className);
      }
    }
  }
}
