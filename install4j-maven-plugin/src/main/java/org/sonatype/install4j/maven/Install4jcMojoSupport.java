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
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.ExecTask;

import java.io.File;

/**
 * Support for install4jc-based tasks.
 *
 * @since 1.0
 */
public abstract class Install4jcMojoSupport
    extends MojoSupport
{
    /**
     * @parameter expression="${install4j.skip}" default-value="false"
     */
    protected boolean skip;

    /**
     * @parameter expression="${install4j.home}"
     * @required
     */
    protected File installDir;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
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

        // FIXME: This apparently just stopped working... WTF
        //Chmod chmod = ant.createTask(Chmod.class);
        //chmod.setExecutable(install4jc.getAbsolutePath());
        //chmod.setPerm("u+x");
        //chmod.execute();

        ExecTask task = ant.createTask(ExecTask.class);
        task.setExecutable(install4jc.getAbsolutePath());
        execute(ant, task);
    }

    protected abstract void execute(final AntHelper ant, final ExecTask task);
}