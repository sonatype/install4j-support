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
package org.sonatype.install4j.ga;

import java.io.File;

import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.install4j.api.context.Context;
import com.install4j.runtime.beans.actions.update.DownloadFileAction;

/**
 * Configure proxy for Google Analytics support.
 *
 * @since 1.0
 */
public class ConfigureProxyAction
    extends ActionSupport
{
  @Override
  protected boolean execute(final Context context) throws Exception {
    if (GoogleAnalyticsActionSupport.isDisabled()) {
      log.debug("Google Analytics disabled; skipping proxy detection");
      return true;
    }

    // Trigger a file download action, so that proxy configuration can be detected
    log.debug("Detecting proxy configuration");
    DownloadFileAction action = new DownloadFileAction();
    action.setUrl("http://www.google-analytics.com/__utm.gif"); // just use the tracker's base url w/o any params
    action.setAskForProxy(true);
    action.setDeleteOnExit(true);
    action.setRetryIfInterrupted(true);
    action.setShowFileName(false);
    action.setShowProgress(false);
    action.setShowError(false);
    action.setTargetFile(File.createTempFile("configure-proxy", ".tmp").getAbsolutePath());
    action.execute(context);

    if (log.isDebugEnabled()) {
      log.debug("proxySet: {}", System.getProperty("proxySet"));
      log.debug("proxyHost: {}", System.getProperty("proxyHost"));
      log.debug("proxyPort: {}", System.getProperty("proxyPort"));
    }

    boolean proxySet = Boolean.getBoolean("proxySet");
    if (proxySet) {
      String proxyHost = System.getProperty("proxyHost");
      String proxyPort = System.getProperty("proxyPort");
      if (proxyHost == null && proxyPort == null) {
        log.warn("Invalid proxy configuration; proxySet=true, but proxyHost and proxyPort missing");
        return true;
      }

      String proxy = String.format("%s:%s", proxyHost, proxyPort);
      log.debug("Proxy: {}", proxy);
      JGoogleAnalyticsTracker.setProxy(proxy);
    }

    return true;
  }
}