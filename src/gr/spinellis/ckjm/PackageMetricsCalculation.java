package gr.spinellis.ckjm;

final class PackageMetricsCalculation {
  private PackageMetricsCalculation() {
    throw new AssertionError("Utility class");
  }

  static void calculate(ClassMetricsContainer mc, ClassMetrics clz) {
    String pkg = clz.getPackageName();
    PackageMetrics pm = mc.getPackageMetrics(pkg);
    pm.addClassMetrics(clz);

    calculateCouplings(pm, clz);
    propagateCouplings(mc, pm);
  }

  static private void calculateCouplings(PackageMetrics pm, ClassMetrics cm) {
    pm.updateCa(cm);
    pm.updateCe(cm);
  }

  static private void propagateCouplings(ClassMetricsContainer mc, PackageMetrics pm) {
    String parentPkg = pm.getParentPackageName();
    if (parentPkg == null || parentPkg.isBlank()) {
      return;
    }

    PackageMetrics parentPm = mc.getPackageMetrics(parentPkg);
    parentPm.addPackageMetrics(pm);
    parentPm.updateCa(pm);
    parentPm.updateCe(pm);
    propagateCouplings(mc, parentPm);
  }
}
