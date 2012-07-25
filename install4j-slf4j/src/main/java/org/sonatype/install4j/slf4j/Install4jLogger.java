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

import com.install4j.api.Util;
import com.install4j.api.actions.Action;
import com.install4j.api.screens.Screen;
import org.slf4j.Logger;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

// Based in slf4j SimpleLogger

/**
 * Install4j-slf4j bridge {@link Logger}.
 *
 * @since 1.0
 */
public class Install4jLogger
    extends MarkerIgnoringBase
{
    private static final long serialVersionUID = 1;

    private static final String CONFIGURATION_FILE = "install4jlogger.properties";

    private static final String CONFIGURATION_PREFIX = Install4jLogger.class.getPackage().getName();

    public static final int LEVEL_TRACE = LocationAwareLogger.TRACE_INT;

    public static final int LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;

    public static final int LEVEL_INFO = LocationAwareLogger.INFO_INT;

    public static final int LEVEL_WARN = LocationAwareLogger.WARN_INT;

    public static final int LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

    public static final int LEVEL_ALL = (LEVEL_TRACE - 10);

    public static final int LEVEL_OFF = (LEVEL_ERROR + 10);

    private static final Properties configuration = new Properties();

    private static boolean showLogName = true;

    private static boolean showLevel = false;

    private static String getStringProperty(final String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException e) {
            // Ignore
        }
        return (prop == null) ? configuration.getProperty(name) : prop;
    }

    private static String getStringProperty(final String name, final String defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }

    private static boolean getBooleanProperty(final String name, final boolean defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    static {
        InputStream in = (InputStream) AccessController.doPrivileged(
            new PrivilegedAction()
            {
                public Object run() {
                    ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
                    if (threadCL != null) {
                        return threadCL.getResourceAsStream(CONFIGURATION_FILE);
                    }
                    else {
                        return ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
                    }
                }
            });

        if (null != in) {
            try {
                configuration.load(in);
                in.close();
            }
            catch (java.io.IOException e) {
                // ignored
            }
        }

        showLogName = getBooleanProperty(CONFIGURATION_PREFIX + "showLogName", showLogName);
        showLevel = getBooleanProperty(CONFIGURATION_PREFIX + "showLevel", showLevel);
    }

    protected int currentLogLevel = LEVEL_INFO;

    private Class type;

    private boolean screenOrAction = false;

    Install4jLogger(String name) {
        this.name = name;

        // Install4j's Util.log* helpers need a type (or object) to determine name, so try and load the type here
        this.type = loadType();
        if (type != null) {
            screenOrAction = Screen.class.isAssignableFrom(type) || Action.class.isAssignableFrom(type);
        }

        // Set log level from properties
        String level = getStringProperty(CONFIGURATION_PREFIX + "logger." + name);
        int i = name.lastIndexOf(".");
        while (null == level && i > -1) {
            name = name.substring(0, i);
            level = getStringProperty(CONFIGURATION_PREFIX + "logger." + name);
            i = name.lastIndexOf(".");
        }

        if (null == level) {
            level = getStringProperty(CONFIGURATION_PREFIX + "level");
        }

        if ("all".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_ALL;
        }
        else if ("trace".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_TRACE;
        }
        else if ("debug".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_DEBUG;
        }
        else if ("info".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_INFO;
        }
        else if ("warn".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_WARN;
        }
        else if ("error".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_ERROR;
        }
        else if ("off".equalsIgnoreCase(level)) {
            this.currentLogLevel = LEVEL_OFF;
        }
    }

    private Class loadType() {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            return cl.loadClass(name);
        }
        catch (Exception e) {
            return null;
        }
    }

    private void log(final int level, final String message, final Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }

        StringBuilder buff = new StringBuilder(32);

        if (showLevel) {
            switch (level) {
                case LEVEL_TRACE:
                    buff.append("TRACE");
                    break;
                case LEVEL_DEBUG:
                    buff.append("DEBUG");
                    break;
                case LEVEL_INFO:
                    buff.append("INFO");
                    break;
                case LEVEL_WARN:
                    buff.append("WARN");
                    break;
                case LEVEL_ERROR:
                    buff.append("ERROR");
                    break;
            }
            buff.append(' ');
        }

        // Only append name if no type associated
        if (type == null && showLogName) {
            buff.append(name).append(" - ");
        }

        buff.append(message);

        // If the logger if for a screen or action, null the source to use the current (which should be the screen or action)
        Object source = type;
        if (screenOrAction) {
            source = null;
        }

        // Hand over the details to install4j's Util helper
        if (level == LEVEL_ERROR) {
            Util.logError(source, buff.toString());
        }
        else {
            Util.logInfo(source, buff.toString());
        }
        if (t != null) {
            Util.log(t);
        }
    }

    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    private void formatAndLog(int level, String format, Object[] argArray) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, argArray);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    protected boolean isLevelEnabled(int logLevel) {
        return (logLevel >= currentLogLevel);
    }

    public boolean isTraceEnabled() {
        return isLevelEnabled(LEVEL_TRACE);
    }

    public void trace(String msg) {
        log(LEVEL_TRACE, msg, null);
    }

    public void trace(String format, Object param1) {
        formatAndLog(LEVEL_TRACE, format, param1, null);
    }

    public void trace(String format, Object param1, Object param2) {
        formatAndLog(LEVEL_TRACE, format, param1, param2);
    }

    public void trace(String format, Object[] argArray) {
        formatAndLog(LEVEL_TRACE, format, argArray);
    }

    public void trace(String msg, Throwable t) {
        log(LEVEL_TRACE, msg, t);
    }

    public boolean isDebugEnabled() {
        return isLevelEnabled(LEVEL_DEBUG);
    }

    public void debug(String msg) {
        log(LEVEL_DEBUG, msg, null);
    }

    public void debug(String format, Object param1) {
        formatAndLog(LEVEL_DEBUG, format, param1, null);
    }

    public void debug(String format, Object param1, Object param2) {
        formatAndLog(LEVEL_DEBUG, format, param1, param2);
    }

    public void debug(String format, Object[] argArray) {
        formatAndLog(LEVEL_DEBUG, format, argArray);
    }

    public void debug(String msg, Throwable t) {
        log(LEVEL_DEBUG, msg, t);
    }

    public boolean isInfoEnabled() {
        return isLevelEnabled(LEVEL_INFO);
    }

    public void info(String msg) {
        log(LEVEL_INFO, msg, null);
    }

    public void info(String format, Object arg) {
        formatAndLog(LEVEL_INFO, format, arg, null);
    }

    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LEVEL_INFO, format, arg1, arg2);
    }

    public void info(String format, Object[] argArray) {
        formatAndLog(LEVEL_INFO, format, argArray);
    }

    public void info(String msg, Throwable t) {
        log(LEVEL_INFO, msg, t);
    }

    public boolean isWarnEnabled() {
        return isLevelEnabled(LEVEL_WARN);
    }

    public void warn(String msg) {
        log(LEVEL_WARN, msg, null);
    }

    public void warn(String format, Object arg) {
        formatAndLog(LEVEL_WARN, format, arg, null);
    }

    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LEVEL_WARN, format, arg1, arg2);
    }

    public void warn(String format, Object[] argArray) {
        formatAndLog(LEVEL_WARN, format, argArray);
    }

    public void warn(String msg, Throwable t) {
        log(LEVEL_WARN, msg, t);
    }

    public boolean isErrorEnabled() {
        return isLevelEnabled(LEVEL_ERROR);
    }

    public void error(String msg) {
        log(LEVEL_ERROR, msg, null);
    }

    public void error(String format, Object arg) {
        formatAndLog(LEVEL_ERROR, format, arg, null);
    }

    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LEVEL_ERROR, format, arg1, arg2);
    }

    public void error(String format, Object[] argArray) {
        formatAndLog(LEVEL_ERROR, format, argArray);
    }

    public void error(String msg, Throwable t) {
        log(LEVEL_ERROR, msg, t);
    }
}