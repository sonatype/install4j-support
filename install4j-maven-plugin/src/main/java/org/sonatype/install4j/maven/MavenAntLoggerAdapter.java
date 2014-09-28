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

import org.apache.maven.plugin.logging.Log;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;

import java.io.PrintStream;

/**
 * Adapts Ant logging to Maven Logging.
 *
 * @since 1.0
 */
public class MavenAntLoggerAdapter
    extends DefaultLogger
{
  protected Log log;

  public MavenAntLoggerAdapter(final Log log) {
    assert log != null;
    this.log = log;
  }

  @Override
  protected void printMessage(final String message, final PrintStream stream, final int priority) {
    assert message != null;
    assert stream != null;

    switch (priority) {
      case Project.MSG_ERR:
        log.error(message);
        break;

      case Project.MSG_WARN:
        log.warn(message);
        break;

      case Project.MSG_INFO:
        log.info(message);
        break;

      case Project.MSG_VERBOSE:
      case Project.MSG_DEBUG:
        log.debug(message);
        break;
    }
  }
}
