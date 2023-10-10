#!/bin/bash

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
prj_dir="$(dirname $(dirname "$script_dir"))"
# echo "Project directory: $prj_dir"

class_dir="${prj_dir}/build/classes"
dot_dir="${prj_dir}/reports/jdeps/ckjm"

OPTS="--dot-output $dot_dir \
  -verbose:class \
  "
OPTS="$OPTS $@"
# OPTS="--list-deps" # module level dependencies

jdeps $OPTS \
  ${class_dir}/gr/spinellis/ckjm/ClassMetrics.class \
  ${class_dir}/gr/spinellis/ckjm/ClassMetricsContainer.class \
  ${class_dir}/gr/spinellis/ckjm/ClassVisitor.class \
  ${class_dir}/gr/spinellis/ckjm/MethodVisitor.class \
  ${class_dir}/gr/spinellis/ckjm/MetricsFilter.class \
  ${class_dir}/gr/spinellis/ckjm/PackageMetrics.class \
  ${class_dir}/gr/spinellis/ckjm/PackageMetricsCalculation.class \
  ${class_dir}/gr/spinellis/ckjm/report/impl/PrintPlainResults.class \
  ${class_dir}/gr/spinellis/ckjm/report/impl/PrintXmlResults.class \
  ${class_dir}/gr/spinellis/ckjm/report/impl/PrintCsvResults.class \
  ${class_dir}/gr/spinellis/ckjm/report/CkjmOutputHandler.class \
  ${prj_dir}/lib/bcel-6.0.jar

# If dot files generated, render them as images
if [[ $OPTS == *"--dot-output"* ]]; then
  ${prj_dir}/reports/jdeps/render-dots.sh "$dot_dir"
fi
