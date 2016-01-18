/*
 * Copyright (c) 2012-present Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.install4j.maven;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.ExecTask;

/**
 * Compile installers (via install4jc).
 *
 * Execution will skip if <code>install4j.home</code> location is invalid.
 *
 * @see <a href="http://resources.ej-technologies.com/install4j/help/doc/cli/options.html">install4j cli options</a>
 * @since 1.0
 */
@Mojo(name = "compile")
public class CompileMojo
    extends Install4jcMojoSupport
{

  private static final byte[] BOM = new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF};

  /**
   * install4j project file.
   */
  @Parameter(property = "install4j.projectFile", required = true)
  private File projectFile;

  /**
   * Enables verbose mode. In verbose mode, install4j prints out information about internal processes.
   */
  @Parameter(property = "install4j.verbose", defaultValue = "false")
  private boolean verbose;

  /**
   * Enables quiet mode. In quiet mode, no terminal output short of a fatal error will be printed.
   */
  @Parameter(property = "install4j.quiet", defaultValue = "false")
  private boolean quiet;

  /**
   * Enables test mode. In test mode, no media files will be generated in the media file directory.
   */
  @Parameter(property = "install4j.test", defaultValue = "false")
  private boolean test;

  /**
   * Enables incremental test execution.
   */
  @Parameter(property = "install4j.incremental", defaultValue = "false")
  private boolean incremental;

  /**
   * Create additional debug installers for each media file.
   */
  @Parameter(property = "install4j.debug", defaultValue = "false")
  private boolean debug;

  /**
   * Disable LZMA and Pack200 compression.
   */
  @Parameter(property = "install4j.faster", defaultValue = "false")
  private boolean faster;

  /**
   * Disable code signing.
   */
  @Parameter(property = "install4j.disableSigning", defaultValue = "false")
  private boolean disableSigning;

  /**
   * Disable JRE bundling.
   */
  @Parameter(property = "install4j.disableBundling", defaultValue = "false")
  private boolean disableBundling;

  /**
   * Preserve temporary staging directory.
   */
  @Parameter(property = "install4j.preserve", defaultValue = "false")
  private boolean preserve;

  /**
   * Set the Windows keystore password for the private key that is configured for code signing.
   */
  @Parameter(property = "install4j.winKeystorePassword")
  private String winKeystorePassword;

  /**
   * Set the Mac OSX keystore password for the private key that is configured for code signing.
   */
  @Parameter(property = "install4j.macKeystorePassword")
  private String macKeystorePassword;

  /**
   * Override the application version.
   */
  @Parameter(property = "install4j.release", defaultValue = "${project.version}")
  private String release;

  /**
   * The output directory for the generated media files.
   */
  @Parameter(property = "install4j.destination", defaultValue = "${project.build.directory}/media")
  private File destination;

  /**
   * Only build the media files which have been selected in the install4j IDE.
   */
  @Parameter(property = "install4j.buildSelected", defaultValue = "false")
  private boolean buildSelected;

  /**
   * Only build the media files with the specified IDs.
   */
  @Parameter(property = "install4j.buildIds")
  private String buildIds;

  /**
   * Only build media files of the specified type.
   */
  @Parameter(property = "install4j.mediaTypes")
  private String mediaTypes;

  /**
   * Load variable definitions from a file.
   */
  @Parameter(property = "install4j.variableFile")
  private File variableFile;

  /**
   * Override compiler variables with a different values.
   */
  @Parameter
  private Properties variables;

  /**
   * Set custom jvm arguments on compiler.
   */
  @Parameter
  private List<String> jvmArguments;

  /**
   * Attach generated installers.
   *
   * Uses the media id as the classifier.
   */
  @Parameter(property = "install4j.attach", defaultValue = "false")
  private boolean attach;

  @Component
  private MavenProjectHelper projectHelper;

  private File variablesTempFile;

  @Override
  protected void execute(final AntHelper ant, final ExecTask task) throws Exception {
    task.createArg().setFile(projectFile);

    // Fail if any error occurs
    task.setFailonerror(true);
    task.setFailIfExecutionFails(true);

    if (jvmArguments != null) {
      Iterator<String> iter = jvmArguments.iterator();
      while (iter.hasNext()) {
        String arg = String.valueOf(iter.next());
        task.createArg().setValue("-J" + arg);
      }
    }

    if (verbose) {
      task.createArg().setValue("--verbose");
    }

    if (quiet) {
      task.createArg().setValue("--quiet");
    }

    if (test) {
      task.createArg().setValue("--test");
    }

    if (incremental) {
      task.createArg().setValue("--incremental");
    }

    if (debug) {
      task.createArg().setValue("--debug");
    }

    if (faster) {
      task.createArg().setValue("--faster");
    }

    if (disableSigning) {
      task.createArg().setValue("--disable-signing");
    }

    if (disableBundling) {
      task.createArg().setValue("--disable-bundling");
    }

    if (preserve) {
      task.createArg().setValue("--preserve");
    }

    if (winKeystorePassword != null) {
      task.createArg().setValue("--win-keystore-password");
      task.createArg().setValue(winKeystorePassword);
    }

    if (macKeystorePassword != null) {
      task.createArg().setValue("--mac-keystore-password");
      task.createArg().setValue(macKeystorePassword);
    }

    if (release != null) {
      task.createArg().setValue("--release");
      task.createArg().setValue(release);
    }

    if (destination != null) {
      task.createArg().setValue("--destination");
      task.createArg().setFile(destination);
    }

    if (buildSelected) {
      task.createArg().setValue("--build-selected");
    }

    if (buildIds != null) {
      task.createArg().setValue("--build-ids");
      task.createArg().setValue(buildIds);
    }

    if (mediaTypes != null) {
      task.createArg().setValue("--media-types");
      task.createArg().setValue(mediaTypes);
    }

    if (variableFile != null || (variables != null && !variables.isEmpty())) {
      task.createArg().setValue("--var-file");
      task.createArg().setValue(getVariablesFileArgument());
    }

    try {
      task.execute();
    } finally {
      if (variablesTempFile != null) {
        variablesTempFile.delete();
      }
    }

    if (attach) {
      for (AttachedFile attachedFile : parseAttachedFiles()) {
        projectHelper.attachArtifact(
            project,
            attachedFile.type,
            attachedFile.classifier,
            attachedFile.file
        );
      }

      // attach non-media files which the compiler generates
      maybeAttachFile("txt", "output", new File(destination, "output.txt"));
      maybeAttachFile("xml", "updates", new File(destination, "updates.xml"));
      maybeAttachFile("txt", "md5sums", new File(destination, "md5sums"));
    }
  }

  private void createVariablesTempFile() throws IOException {
    variablesTempFile = File.createTempFile("install4jVariables", ".txt");
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(variablesTempFile);
      out.write(BOM); //BOM forces UTF-8 detection for reading
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
      Iterator<Entry<Object, Object>> iter = variables.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<Object, Object> entry = iter.next();
        pw.print(entry.getKey());
        pw.print('=');
        pw.println(entry.getValue());
      }
      pw.flush();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  private void maybeAttachFile(final String type, final String classifier, final File file) {
    if (!file.exists()) {
      log.warn("File missing; unable to attach file: " + file);
      return;
    }
    projectHelper.attachArtifact(project, type, classifier, file);
  }

  private String getVariablesFileArgument() throws IOException {
    StringBuilder buff = new StringBuilder();
    if (variableFile != null) {
      buff.append(variableFile.getPath());
    }
    if (!variables.isEmpty()) {
      createVariablesTempFile();
      if (buff.length() > 0) {
        buff.append(";");
      }
      buff.append(variablesTempFile.getPath());
    }
    return buff.toString();
  }

  private static class AttachedFile
  {
    public final File file;

    public final String type;

    public final String classifier;

    private AttachedFile(final File file, final String classifier) {
      this.file = file;
      this.type = getType(file);
      // TODO: Should ensure this is a valid classifier (replace spaces, etc).
      this.classifier = classifier;
    }

    private static String getType(final File file) {
      String path = file.getAbsolutePath();

      // special case for compound '.' extensions
      if (path.endsWith(".tar.gz")) {
        return "tar.gz";
      }
      else if (path.endsWith(".tar.bz2")) {
        return "tar.bz2";
      }

      int i = path.lastIndexOf(".");
      return path.substring(i + 1, path.length());
    }
  }

  private List<AttachedFile> parseAttachedFiles() throws Exception {
    File file = new File(destination, "output.txt");
    if (!file.exists()) {
      log.warn("Missing output.txt file: " + file);
      return Collections.emptyList();
    }

    log.debug("Parsing: " + file);

    BufferedReader reader = new BufferedReader(new FileReader(file));
    List<AttachedFile> files = new ArrayList<AttachedFile>();
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.startsWith("#")) {
        // ignore comments
        continue;
      }

      log.debug("Read: " + line);

      // fields are tab-delimited:
      // id | media file type | display name | media file path
      String[] parts = line.split("\t");
      AttachedFile attachedFile = new AttachedFile(
          normalize(destination, parts[3]), // media file path
          parts[0]  // id
      );
      files.add(attachedFile);
    }

    return files;
  }

  /**
   * Normalize given path to given base.
   *
   * This impacts some plugins which don't fully canonicalize before making assumptions about parent paths
   * and w/o this can case some behavior differences depending on the path given to maven to execute
   * if it had been given ./foo/pom.xml or foo/pom.xml.
   *
   * If base is "/foo/./bar" and path is "/foo/bar/baz", will normalize to "/foo/./bar/baz", etc.
   */
  private File normalize(final File base, final String path) throws Exception {
    String basePath = base.getCanonicalPath();
    String filePath = new File(path).getCanonicalPath();
    if (filePath.startsWith(basePath)) {
      String relPath = filePath.substring(basePath.length() + 1, filePath.length());
      return new File(base, relPath);

    }
    else {
      return new File(path);
    }
  }
}