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

package org.sonatype.install4j.slf4j;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

// Based in slf4j SimpleLoggerFactory

/**
 * install4j-slf4j bridge {@link ILoggerFactory}.
 *
 * @since 1.0
 */
public class Install4jLoggerFactory
    implements ILoggerFactory
{
  private final Map<String, Logger> loggers = new HashMap<String, Logger>();

  public Logger getLogger(String name) {
    Logger logger;
    synchronized (this) {
      logger = loggers.get(name);
      if (logger == null) {
        logger = new Install4jLogger(name);
        loggers.put(name, logger);
      }
    }
    return logger;
  }
}