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

package org.sonatype.install4j.ga;

import com.dmurph.tracking.JGoogleAnalyticsTracker;

/**
 * Support for Google Analytics tracking actions.
 *
 * @see TrackerFactory
 * @since 1.0
 */
public abstract class GoogleAnalyticsActionSupport
    extends ActionSupport
{
  private static final TrackerFactory factory = new TrackerFactory();

  private static final boolean disabled = getFlag(GoogleAnalyticsActionSupport.class, "disable");

  private String trackingCode;

  public GoogleAnalyticsActionSupport() {
    this.reportFailure = false; // do not show users failure details
  }

  public String getTrackingCode() {
    return trackingCode;
  }

  public void setTrackingCode(final String trackingCode) {
    this.trackingCode = trackingCode;
  }

  public static TrackerFactory getFactory() {
    return factory;
  }

  public static boolean isDisabled() {
    return disabled;
  }

  protected JGoogleAnalyticsTracker getTracker() {
    return factory.get(getTrackingCode());
  }
}