/*
 * Copyright (c) 2008-2015 Sonatype, Inc. All rights reserved.
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
package org.sonatype.install4j.common;

import java.io.File;

import com.install4j.api.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helpers to access context variables.
 *
 * @since 1.0.9
 */
public class Variables
{
  private static final Logger log = LoggerFactory.getLogger(Variables.class);

  public static String getStringVariable(final Context context, final String name) {
    Object tmp = context.getVariable(name);
    String value = null;
    if (tmp != null) {
      value = tmp.toString();
    }
    log.debug("Get (string) variable: {}={}", name, value);
    return value;
  }

  public static File getFileVariable(final Context context, final String name) {
    String tmp = getStringVariable(context, name);
    File value = null;
    if (tmp != null) {
      value = new File(tmp);
    }
    log.debug("Get (file) variable: {}={}", name, value);
    return value;
  }

  public static Integer getIntegerVariable(final Context context, final String name) {
    String tmp = getStringVariable(context, name);
    Integer value = null;
    if (tmp != null) {
      value = Integer.parseInt(tmp);
    }
    log.debug("Get (integer) variable: {}={}", name, value);
    return value;
  }
}
