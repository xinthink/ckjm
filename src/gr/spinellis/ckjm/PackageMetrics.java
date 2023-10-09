package gr.spinellis.ckjm;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PackageMetrics implements Comparable<PackageMetrics> {
  private final String packageName;

  /**
   * The set of child package metrics.
   */
  private final Set<PackageMetrics> packageMetrics = new TreeSet<>();

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

  public PackageMetrics(String packageName) {
    this.packageName = packageName;
    afferentCoupledClasses = new HashSet<>();
    efferentCoupledClasses = new HashSet<>();
  }

  /**
   * Return the name of this package.
   */
  public String getPackageName() {
    return packageName;
  }

  /**
   * Return the name of the parent package.
   */
  public String getParentPackageName() {
    int lastDot = packageName.lastIndexOf('.');
    return lastDot > -1 ? packageName.substring(0, lastDot) : "";
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
   * Return the set of child packages.
   */
  public Set<PackageMetrics> getPackageMetrics() {
    return packageMetrics;
  }

  /**
   * Add a child package (metrics) to this package.
   */
  public void addPackageMetrics(PackageMetrics pm) {
    packageMetrics.add(pm);
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

  /**
   * Refresh the package level EC metric with the given ClassMetrics.
   */
  public void updateCe(ClassMetrics cm) {
    for (String className : cm.getEfferentCoupledClasses()) {
      if (!className.startsWith(packageName)) {
        efferentCoupledClasses.add(className);
      }
    }
  }

  /**
   * Refresh the package level AC metric with the given ClassMetrics.
   */
  public void updateCa(ClassMetrics cm) {
    for (String className : cm.getAfferentCoupledClasses()) {
      if (!className.startsWith(packageName)) {
        afferentCoupledClasses.add(className);
      }
    }
  }

  /**
   * Refresh the package level EC metric with the given child PackageMetrics.
   */
  public void updateCe(PackageMetrics childPm) {
    for (String className : childPm.efferentCoupledClasses) {
      if (!className.startsWith(packageName)) {
        efferentCoupledClasses.add(className);
      }
    }
  }

  /**
   * Refresh the package level AC metric with the given child PackageMetrics.
   */
  public void updateCa(PackageMetrics childPm) {
    for (String className : childPm.afferentCoupledClasses) {
      if (!className.startsWith(packageName)) {
        afferentCoupledClasses.add(className);
      }
    }
  }

  @Override
  public int compareTo(PackageMetrics o) {
    return packageName.compareTo(o.packageName);
  }
}
