/*
 * (C) Copyright 2005 Diomidis Spinellis
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

package gr.spinellis.ckjm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import gr.spinellis.ckjm.report.CkjmOutputHandler;


/**
 * A container of class metrics mapping class names to their metrics.
 * This class contains the the metrics for all class's during the filter's
 * operation.  Some metrics need to be updated as the program processes
 * other classes, so the class's metrics will be recovered from this
 * container to be updated.
 *
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 * @version $Revision: 1.9 $
 */
class ClassMetricsContainer {

  /**
   * The map from class names to the corresponding metrics
   */
  private final Map<String, ClassMetrics> m = new HashMap<>();

  /**
   * The map from package names to the corresponding metrics
   */
  private final Map<String, PackageMetrics> pm = new HashMap<>();

  /**
   * The set of root packages (no parent package)
   */
  private final Set<String> rootPackages = new TreeSet<>();

  /**
   * Return a class's metrics
   */
  public ClassMetrics getMetrics(String name) {
    ClassMetrics cm = m.get(name);
    if (cm == null) {
      cm = new ClassMetrics(name);
      m.put(name, cm);
    }
    return cm;
  }

  /**
   * Return a package's metrics
   */
  public PackageMetrics getPackageMetrics(String name) {
    PackageMetrics m = pm.get(name);
    if (m == null) {
      m = new PackageMetrics(name);
      pm.put(name, m);
    }

    String parent = m.getParentPackageName();
    if (parent == null || parent.isBlank()) {
      rootPackages.add(name);
    }
    return m;
  }

  /**
   * Print the metrics of all the visited classes.
   */
  public void printMetrics(CkjmOutputHandler handler) {
    for (String rootPackage : rootPackages) {
      PackageMetrics p = pm.get(rootPackage);
      if (p != null) printMetrics(p, handler);
    }
  }

  private void printMetrics(PackageMetrics pm, CkjmOutputHandler handler) {
    handler.handlePackage(pm);

    for (ClassMetrics cm : pm.getClassMetrics()) {
      if (cm != null) handler.handleClass(cm);
    }

    for (PackageMetrics child : pm.getPackageMetrics()) {
      if (child != null) printMetrics(child, handler);
    }

    handler.endOfPackage(pm);
  }
}
