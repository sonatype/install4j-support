/*
 * Copyright (c) 2007-2012 Sonatype, Inc. All rights reserved.
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionConstraint;
import org.eclipse.aether.version.VersionScheme;

import java.io.File;

/**
 * Helper to get version information from install4j.
 *
 * @since 1.0.5
 */
public class VersionHelper
{
  /**
   * Allows install4j 5.1.2+
   */
  private static final String VERSION_CONSTRAINT = "[5.1.2,)";

  private final Log log;

  public VersionHelper(final Log log) {
    this.log = log;
  }

  /**
   * Get the version of install4 by running {@code install4jc --version}.
   *
   * @param ant        Ant task helper
   * @param install4jc File pointing at the {@code install4jc} executable binary.
   */
  public String fetchVersion(final AntHelper ant, final File install4jc) {
    // Sanity check, ask install4jc for its version
    ExecTask task = ant.createTask(ExecTask.class);
    task.setExecutable(install4jc.getAbsolutePath());
    task.createArg().setValue("--version");
    // ensure we have a fresh property to return the version details in
    String versionProperty = "install4j.version-" + System.currentTimeMillis();
    task.setOutputproperty(versionProperty);
    task.execute();
    return versionProperty;
  }

  /**
   * Parse version in format:
   *
   * <pre>
   * install4j version _version-#_ (build _build-#_), built on _date_
   * </pre>
   *
   * Ignores any lines before.
   *
   * @param rawVersion Text returned from {@code install4jc --version}
   * @return The {@code _version-#_} portion of the raw version input.
   */
  public String parseVersion(final String rawVersion) {
    log.debug("Parsing version: " + rawVersion);

    String[] lines = rawVersion.split("\n");
    for (String line : lines) {
      line = line.trim();

      if (line.startsWith("install4j version ")) { // trailing space in string on purpose
        String[] parts = line.split("\\s");
        // ignore the build #
        return parts[2];
      }
    }

    throw new RuntimeException("Unable to parse version from input: " + rawVersion);
  }

  /**
   * Ensure the install4j version is compatible.
   *
   * @param rawVersion Text returned from {@code install4jc --version}
   * @throws Exception Version is not compatible
   */
  public void ensureVersionCompatible(final String rawVersion) throws Exception {
    String version = parseVersion(rawVersion);
    VersionScheme scheme = new GenericVersionScheme();
    VersionConstraint constraint = scheme.parseVersionConstraint(VERSION_CONSTRAINT);
    Version _version = scheme.parseVersion(version);
    log.debug("Version: " + _version);

    if (!constraint.containsVersion(_version)) {
      log.error("Incompatible install4j version detected");
      log.error("Raw version: " + rawVersion);
      log.error("Detected version: " + _version);
      log.error("Compatible version constraint: " + constraint);
      throw new MojoExecutionException("Unsupported install4j version: " + rawVersion);
    }
  }
}