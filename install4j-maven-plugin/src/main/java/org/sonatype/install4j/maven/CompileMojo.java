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

import com.install4j.Install4JTask;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * Compile installers.
 *
 * @goal compile
 *
 * @since 1.0
 */
public class CompileMojo
    extends MojoSupport
{
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
     * @parameter expression="${install4j.destination}" default-value="${project.build.directory}/generated-files/install4j"
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
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    @Override
    protected void doExecute() throws Exception {
        AntHelper ant = new AntHelper(this, project);

        Install4JTask compiler = ant.createTask(Install4JTask.class);
        compiler.setProjectFile(projectFile);
        compiler.setVerbose(verbose);
        compiler.setQuiet(quiet);
        compiler.setTest(test);
        compiler.setDebug(debug);
        compiler.setFaster(faster);
        compiler.setDisableSigning(disableSigning);
        compiler.setWinKeystorePassword(winKeystorePassword);
        compiler.setMacKeystorePassword(macKeystorePassword);
        compiler.setDestination(destination);
        compiler.setBuildSelected(buildSelected);
        compiler.setBuildIds(buildIds);
        compiler.setMediaTypes(mediaTypes);
        compiler.setVariablefile(variableFile);
        compiler.execute();
    }
}