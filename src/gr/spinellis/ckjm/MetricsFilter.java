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

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import gr.spinellis.ckjm.report.CkjmOutputHandler;
import gr.spinellis.ckjm.report.impl.PrintPlainResults;
import gr.spinellis.ckjm.report.impl.PrintXmlResults;

/**
 * Convert a list of classes into their metrics.
 * Process standard input lines or command line arguments
 * containing a class file name or a jar file name,
 * followed by a space and a class file name.
 * Display on the standard output the name of each class, followed by its
 * six Chidamber Kemerer metrics:
 * WMC, DIT, NOC, CBO, RFC, LCOM
 *
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 * @version $Revision: 1.9 $
 * @see ClassMetrics
 */
public class MetricsFilter {
  /**
   * True if the measurements should include calls to the Java JDK into account
   */
  private static boolean includeJdk = false;

  /**
   * True if the reports should only include public classes
   */
  private static boolean onlyPublic = false;

  private static String xmlReportTarget;

  private static String csvReportTarget;

  /**
   * Return true if the measurements should include calls to the Java JDK into account
   */
  public static boolean isJdkIncluded() {
    return includeJdk;
  }

  /**
   * Return true if the measurements should include all classes
   */
  public static boolean includeAll() {
    return !onlyPublic;
  }

  /**
   * Load and parse the specified class.
   * The class specification can be either a class file name, or
   * a jarfile, followed by space, followed by a class file name.
   */
  static void processClass(ClassMetricsContainer cm, String clspec) {
    int spc;
    JavaClass jc = null;

    if ((spc = clspec.indexOf(' ')) != -1) {
      String jar = clspec.substring(0, spc);
      clspec = clspec.substring(spc + 1);
      try {
        jc = new ClassParser(jar, clspec).parse();
      } catch (IOException e) {
        System.err.println("Error loading " + clspec + " from " + jar + ": " + e);
      }
    } else {
      try {
        jc = new ClassParser(clspec).parse();
      } catch (IOException e) {
        System.err.println("Error loading " + clspec + ": " + e);
      }
    }
    if (jc != null) {
      ClassVisitor visitor = new ClassVisitor(jc, cm);
      visitor.start();
      visitor.end();
    }
  }

  static void processClass(ClassMetricsContainer cm, InputStream stream, String clspec) {
    JavaClass jc = null;

    try {
      jc = new ClassParser(stream, clspec).parse();
    } catch (IOException e) {
      System.err.println("Error loading " + clspec + ": " + e);
    }
    if (jc != null) {
      ClassVisitor visitor = new ClassVisitor(jc, cm);
      visitor.start();
      visitor.end();
    }
  }

  /**
   * The interface for other Java based applications.
   * Implement the outputhandler to catch the results
   *
   * @param files         Class files to be analyzed
   * @param outputHandler An implementation of the CkjmOutputHandler interface
   */
  public static void runMetrics(String[] files, CkjmOutputHandler outputHandler) {
    ClassMetricsContainer cm = new ClassMetricsContainer();

    for (String file : files) {
      processClass(cm, file);
    }
    cm.printMetrics(outputHandler);
  }

  /**
   * The filter's main body.
   * Process command line arguments and the standard input.
   */
  public static void main(String[] argv) {
    int argp = 0;

    if (argv.length > argp && argv[argp].equals("-s")) {
      includeJdk = true;
      argp++;
    }
    if (argv.length > argp && argv[argp].equals("-p")) {
      onlyPublic = true;
      argp++;
    }
    if (argv.length > argp && argv[argp].equals("--xml")) {
      xmlReportTarget = argv[++argp];
      argp++;
    }
    if (argv.length > argp && argv[argp].equals("--csv")) {
      csvReportTarget = argv[++argp];
      argp++;
    }

    ClassMetricsContainer cm = new ClassMetricsContainer();

//    if (argv.length == argp) {
//      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//      try {
//        String s;
//        while ((s = in.readLine()) != null) {
//          processClass(cm, s);
//        }
//      } catch (Exception e) {
//        System.err.println("Error reading line: " + e);
//        System.exit(1);
//      }
//    }

    for (int i = argp; i < argv.length; i++) {
      String file = argv[i];
      if (file.endsWith(".jar")) {
        parseJarFile(cm, file);
      } else {
        processClass(cm, file);
      }
    }

    CkjmOutputHandler handler = new PrintPlainResults(System.out);
    cm.printMetrics(handler);

    if (xmlReportTarget != null) {
      try {
        PrintXmlResults xmlHandler = new PrintXmlResults(new PrintStream(new FileOutputStream(xmlReportTarget)));
        xmlHandler.printHeader();
        cm.printMetrics(xmlHandler);
        xmlHandler.printFooter();
      } catch (IOException e) {
        System.err.println("Failed to generate XML report:" + e);
      }
    }
  }

  private static void parseJarFile(ClassMetricsContainer cm, String jar) {
    Enumeration<JarEntry> entries;
    try (JarFile jarFile = new JarFile(jar)) {
      entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (entry.getName().endsWith(".class")) {
          processClass(cm, jarFile.getInputStream(entry), entry.getName());
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading " + jar + ": " + e);
    }
  }
}
