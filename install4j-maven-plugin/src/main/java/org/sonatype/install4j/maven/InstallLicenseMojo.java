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

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.tools.ant.taskdefs.ExecTask;

/**
 * Update the install4j license key (via install4jc --license).
 *
 * Execution will skip if <code>licenseKey</code> parameter is not configured, or the <code>install4j.home</code>
 * location is invalid.
 *
 * @since 1.0
 */
@Mojo(name = "install-license")
public class InstallLicenseMojo
    extends Install4jcMojoSupport
{
  /**
   * install4j license key.
   */
  @Parameter(property = "install4j.licenseKey")
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