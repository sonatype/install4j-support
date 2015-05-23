/*
 * Copyright (c) 2008-2015 Sonatype, Inc.
 *
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/pro/attributions
 * Sonatype and Sonatype Nexus are trademarks of Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation.
 * M2Eclipse is a trademark of the Eclipse Foundation. All other trademarks are the property of their respective owners.
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
