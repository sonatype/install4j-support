/*
 * Copyright (c) 2012-present Sonatype, Inc. All rights reserved.
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
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import org.sonatype.install4j.slf4j.Install4jLoggerFactory;

/**
 * install4j-slf4j bridge Logger binder.
 *
 * @since 1.0
 */
@SuppressWarnings("UnusedDeclaration")
public class StaticLoggerBinder
    implements LoggerFactoryBinder
{
  private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

  public static StaticLoggerBinder getSingleton() {
    return SINGLETON;
  }

  public static String REQUESTED_API_VERSION = "1.6";  // !final to avoid compile static folding

  private final ILoggerFactory loggerFactory = new Install4jLoggerFactory();

  private StaticLoggerBinder() {
    super();
  }

  public ILoggerFactory getLoggerFactory() {
    return loggerFactory;
  }

  public String getLoggerFactoryClassStr() {
    return Install4jLoggerFactory.class.getName();
  }
}