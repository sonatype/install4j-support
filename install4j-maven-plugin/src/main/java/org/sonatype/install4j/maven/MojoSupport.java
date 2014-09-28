/*
 * Copyright (c) 2007-2014 Sonatype, Inc. All rights reserved.
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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * Support for {@link org.apache.maven.plugin.Mojo} implementations.
 *
 * @since 1.0
 */
public abstract class MojoSupport
    extends AbstractMojo
{
  protected Log log;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    this.log = getLog();

    try {
      doExecute();
    }
    catch (Exception e) {
      if (e instanceof MojoExecutionException) {
        throw (MojoExecutionException) e;
      }
      else if (e instanceof MojoFailureException) {
        throw (MojoFailureException) e;
      }
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  protected abstract void doExecute() throws Exception;
}