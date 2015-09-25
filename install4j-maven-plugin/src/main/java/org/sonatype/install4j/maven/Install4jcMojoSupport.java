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

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.codehaus.plexus.util.Os;

/**
 * Support for install4jc-based tasks.
 *
 * Compatible with install4j version 6.0.0 or higher.
 *
 * @since 1.0
 */
public abstract class Install4jcMojoSupport
    extends MojoSupport
{
  /**
   * Skip execution.
   */
  @Parameter(property = "install4j.skip", defaultValue = "false")
  protected boolean skip;

  /**
   * The location of the install4j installation.
   */
  @Parameter(property = "install4j.home")
  protected File installDir;

  /**
   * Fail if the installation is missing.
   */
  @Parameter(property = "install4j.failIfMissing", defaultValue = "false")
  protected boolean failIfMissing;

  @Component
  protected MavenProject project;

  @Override
  protected void doExecute() throws Exception {
    if (skip) {
      log.warn("Skipping execution");
      return;
    }

    AntHelper ant = new AntHelper(this, project);

    if (installDir == null) {
      maybeFailIfMissing("Install directory not configured");
      return;
    }
    if (!installDir.exists()) {
      maybeFailIfMissing("Invalid install directory: " + installDir);
      return;
    }

    log.debug("install4j installation directory: " + installDir);

    boolean windows = Os.isFamily(Os.FAMILY_WINDOWS);
    File install4jc = new File(installDir, "bin/install4jc" + (windows ? ".exe" : ""));

    if (!install4jc.exists()) {
      maybeFailIfMissing("Missing install4j compiler executable: " + install4jc);
      return;
    }

    log.debug("install4jc: " + install4jc);

    // ensure the binary is executable
    // FIXME: This can fail (well complain really, if we don't have perms to change the file, should check)
    ant.chmod(install4jc, "u+x");

    // ensure version is compatible
    VersionHelper versionHelper = new VersionHelper(log);
    String versionProperty = versionHelper.fetchVersion(ant, install4jc);
    versionHelper.ensureVersionCompatible(ant.getProperty(versionProperty));

    ExecTask task = ant.createTask(ExecTask.class);
    task.setExecutable(install4jc.getAbsolutePath());
    execute(ant, task);
  }

  protected abstract void execute(final AntHelper ant, final ExecTask task) throws Exception;

  private void maybeFailIfMissing(final String message) throws MojoExecutionException {
    if (failIfMissing) {
      log.error(message);
      throw new MojoExecutionException(message);
    }
    else {
      log.warn(message);
    }
  }
}