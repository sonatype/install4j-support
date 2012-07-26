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

import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.ant.taskdefs.ExecTask;

import java.io.File;

/**
 * Compile installers (via install4jc).
 *
 * @goal compile
 *
 * @since 1.0
 */
public class CompileMojo
    extends MojoSupport
{
    /**
     * @parameter expression="${install4j.home}"
     * @required
     */
    private File installDir;

    /**
     * @parameter expression="${install4j.projectFile}"
     * @required
     */
    private File projectFile;

    /**
     * @parameter expression="${install4j.verbose}" default-value="false"
     */
    private boolean verbose;

    /**
     * @parameter expression="${install4j.quiet}" default-value="false"
     */
    private boolean quiet;

    /**
     * @parameter expression="${install4j.test}" default-value="false"
     */
    private boolean test;

    /**
     * @parameter expression="${install4j.debug}" default-value="false"
     */
    private boolean debug;

    /**
     * @parameter expression="${install4j.faster}" default-value="false"
     */
    private boolean faster;

    /**
     * @parameter expression="${install4j.disableSigning}" default-value="false"
     */
    private boolean disableSigning;

    /**
     * @parameter expression="${install4j.winKeystorePassword}"
     */
    private String winKeystorePassword;

    /**
     * @parameter expression="${install4j.macKeystorePassword}"
     */
    private String macKeystorePassword;

    /**
     * @parameter expression="${install4j.release}" default-value="${project.version}"
     */
    private String release;

    /**
     * @parameter expression="${install4j.destination}" default-value="${project.build.directory}/media"
     */
    private File destination;

    /**
     * @parameter expression="${install4j.buildSelected}" default-value="false"
     */
    private boolean buildSelected;

    /**
     * @parameter expression="${install4j.buildIds}"
     */
    private String buildIds;

    /**
     * @parameter expression="${install4j.mediaTypes}"
     */
    private String mediaTypes;

    /**
     * @parameter expression="${install4j.variableFile}"
     */
    private File variableFile;

    /**
     * @parameter expression="${install4j.attach}" default-value="true"
     */
    private boolean attach;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    @Override
    protected void doExecute() throws Exception {
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

        ExecTask compiler = ant.createTask(ExecTask.class);
        compiler.setExecutable(install4jc.getAbsolutePath());
        compiler.createArg().setFile(projectFile);

        if (verbose) {
            compiler.createArg().setValue("--verbose");
        }

        if (quiet) {
            compiler.createArg().setValue("--quiet");
        }

        if (test) {
            compiler.createArg().setValue("--test");
        }

        if (debug) {
            compiler.createArg().setValue("--debug");
        }

        if (faster) {
            compiler.createArg().setValue("--faster");
        }

        if (disableSigning) {
            compiler.createArg().setValue("--disable-signing");
        }

        if (winKeystorePassword != null) {
            compiler.createArg().setValue("--win-keystore-password");
            compiler.createArg().setValue(winKeystorePassword);
        }

        if (winKeystorePassword != null) {
            compiler.createArg().setValue("--mac-keystore-password");
            compiler.createArg().setValue(macKeystorePassword);
        }

        compiler.createArg().setValue("--destination");
        compiler.createArg().setFile(destination);

        if (buildSelected) {
            compiler.createArg().setValue("--build-selected");
        }

        if (buildIds != null) {
            compiler.createArg().setValue("--build-ids");
            compiler.createArg().setValue(buildIds);
        }

        if (mediaTypes != null) {
            compiler.createArg().setValue("--media-types");
            compiler.createArg().setValue(mediaTypes);
        }

        if (variableFile != null) {
            compiler.createArg().setValue("--var-file");
            compiler.createArg().setFile(variableFile);
        }

        // TODO: Add support for -D variables

        compiler.execute();

        if (attach) {
            // TODO: A bit tricky since we depend on the files to determine what to attach
            // TODO: Use updates.xml to determine what files/classifiers instead
        }
    }
}