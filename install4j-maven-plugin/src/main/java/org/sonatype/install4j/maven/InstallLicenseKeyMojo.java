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
import org.apache.tools.ant.taskdefs.ExecTask;

import java.io.File;

/**
 * Install license key (via install4jc).
 *
 * @goal install-license
 *
 * @since 1.0
 */
public class InstallLicenseKeyMojo
    extends MojoSupport
{
    /**
     * @parameter expression="${install4j.home}"
     * @required
     */
    private File installDir;

    /**
     * @parameter expression="${install4j.licenseKey}"
     * @required
     */
    private String licenseKey;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

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

        compiler.createArg().setValue("--license");
        compiler.createArg().setValue(licenseKey);

        compiler.execute();
    }
}