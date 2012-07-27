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
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.sonatype.aether.util.version.GenericVersionScheme;
import org.sonatype.aether.version.Version;
import org.sonatype.aether.version.VersionConstraint;
import org.sonatype.aether.version.VersionScheme;

import java.io.File;

/**
 * Support for install4jc-based tasks.
 *
 * Compatible with install4j version 5.1.2 or higher.
 *
 * @since 1.0
 */
public abstract class Install4jcMojoSupport
    extends MojoSupport
{
    private static final String INSTALL4J_VERSION = "install4j.version";

    /**
     * Skip execution.
     */
    @Parameter(property = "install4j.skip", defaultValue = "false")
    protected boolean skip;

    /**
     * The location of the install4j installation.
     */
    @Parameter(property = "install4j.home", required = true)
    protected File installDir;

    @Component
    protected MavenProject project;

    @Override
    protected void doExecute() throws Exception {
        if (skip) {
            log.warn("Skipping execution");
            return;
        }

        AntHelper ant = new AntHelper(this, project);

        if (!installDir.exists()) {
            log.warn("Invalid install directory; skipping: " + installDir);
            return;
        }

        log.debug("Install4j installation directory: " + installDir);

        File install4jc = new File(installDir, "bin/install4jc");

        if (!install4jc.exists()) {
            log.warn("Missing Install4j compiler executable: " + install4jc);
            return;
        }

        log.debug("Install4jc: " + install4jc);

        ant.chmod(install4jc, "u+x");

        // Sanity check, ask install4jc for its version
        ExecTask task = ant.createTask(ExecTask.class);
        task.setExecutable(install4jc.getAbsolutePath());
        task.createArg().setValue("--version");
        task.setOutputproperty(INSTALL4J_VERSION);
        task.execute();
        ensureVersionCompatible(ant.getProperty(INSTALL4J_VERSION));

        task = ant.createTask(ExecTask.class);
        task.setExecutable(install4jc.getAbsolutePath());
        execute(ant, task);
    }

    // TODO: Convert helpers into components so we can test them, have to sort out slf4j maven integration

    /**
     * Parse version in format:
     *
     * install4j version _version-#_ (build _build-#_), built on _date_
     */
    private String parseVersion(final String rawVersion) {
        log.debug("Parsing version: " + rawVersion);
        String[] parts = rawVersion.split("\\s");
        boolean valid = parts.length > 3 && parts[0].equals("install4j") && parts[1].equals("version");
        if (!valid) {
            throw new RuntimeException("Unable to parse version from input: " + rawVersion);
        }
        // ignore the build #
        return parts[2];
    }

    private void ensureVersionCompatible(final String rawVersion) throws Exception {
        String version = parseVersion(rawVersion);
        VersionScheme scheme = new GenericVersionScheme();
        VersionConstraint constraint = scheme.parseVersionConstraint("[5.1.2,)"); // allow 5.1.2+
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

    protected abstract void execute(final AntHelper ant, final ExecTask task) throws Exception;
}