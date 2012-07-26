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

import org.apache.tools.ant.taskdefs.ExecTask;

/**
 * Install license key (via install4jc --license).
 *
 * @goal install-license
 *
 * @since 1.0
 */
public class InstallLicenseMojo
    extends Install4jcMojoSupport
{
    /**
     * @parameter expression="${install4j.licenseKey}"
     */
    private String licenseKey;

    @Override
    protected void execute(final AntHelper ant, final ExecTask task) throws Exception {
        if (licenseKey == null) {
            log.warn("Missing install4j.licenseKey; skipping");
            return;
        }
        task.createArg().setValue("--license");
        task.createArg().setValue(licenseKey);
        task.execute();
    }
}